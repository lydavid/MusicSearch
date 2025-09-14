package ly.david.musicsearch.data.repository.label

import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class LabelRepositoryImpl(
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : LabelRepository {

    override suspend fun lookupLabel(
        labelId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): LabelDetailsModel {
        if (forceRefresh) {
            delete(labelId)
        }

        val cachedData = getCachedData(labelId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val labelMusicBrainzModel = lookupApi.lookupLabel(labelId)
            cache(
                oldId = labelId,
                label = labelMusicBrainzModel,
                lastUpdated = lastUpdated,
            )
            getCachedData(labelMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(labelId: String): LabelDetailsModel? {
        if (!relationRepository.visited(labelId)) return null
        val label = labelDao.getLabelForDetails(labelId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(labelId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.LABEL,
            mbid = labelId,
        )

        return label.copy(
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        labelDao.withTransaction {
            labelDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        oldId: String,
        label: LabelMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        labelDao.withTransaction {
            labelDao.upsert(
                oldId = oldId,
                label = label,
            )

            aliasDao.insertAll(listOf(label))

            val relationWithOrderList = label.relations.toRelationWithOrderList(label.id)
            relationRepository.insertAllUrlRelations(
                entityId = label.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
