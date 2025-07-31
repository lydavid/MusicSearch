package ly.david.musicsearch.data.repository.label

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

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

        val label = labelDao.getLabelForDetails(labelId)
        val urlRelations = relationRepository.getRelationshipsByType(labelId)
        val visited = relationRepository.visited(labelId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntity.LABEL,
            mbid = labelId,
        )

        if (label != null && visited) {
            return label.copy(
                urls = urlRelations,
                aliases = aliases,
            )
        }

        val labelMusicBrainzModel = lookupApi.lookupLabel(labelId)
        cache(
            label = labelMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupLabel(
            labelId = labelId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(id: String) {
        labelDao.withTransaction {
            labelDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        label: LabelMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        labelDao.withTransaction {
            labelDao.insert(label)

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
