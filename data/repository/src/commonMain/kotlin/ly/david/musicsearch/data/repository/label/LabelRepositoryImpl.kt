package ly.david.musicsearch.data.repository.label

import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class LabelRepositoryImpl(
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : LabelRepository {

    override suspend fun lookupLabel(
        labelId: String,
        forceRefresh: Boolean,
    ): LabelDetailsModel {
        if (forceRefresh) {
            delete(labelId)
        }

        val label = labelDao.getLabelForDetails(labelId)
        val urlRelations = relationRepository.getRelationshipsByType(labelId)
        val visited = relationRepository.visited(labelId)
        if (label != null && visited) {
            return label.copy(urls = urlRelations)
        }

        val labelMusicBrainzModel = lookupApi.lookupLabel(labelId)
        cache(labelMusicBrainzModel)
        return lookupLabel(labelId, false)
    }

    private fun delete(id: String) {
        labelDao.withTransaction {
            labelDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(label: LabelMusicBrainzModel) {
        labelDao.withTransaction {
            labelDao.insert(label)

            val relationWithOrderList = label.relations.toRelationWithOrderList(label.id)
            relationRepository.insertAllUrlRelations(
                entityId = label.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
