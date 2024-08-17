package ly.david.musicsearch.data.repository.label

import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class LabelRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
) : LabelRepository {

    override suspend fun lookupLabel(labelId: String): LabelDetailsModel {
        val label = labelDao.getLabelForDetails(labelId)
        val urlRelations = relationRepository.getEntityUrlRelationships(labelId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(labelId)
        if (label != null && hasUrlsBeenSavedForEntity) {
            return label.copy(urls = urlRelations)
        }

        val labelMusicBrainzModel = musicBrainzApi.lookupLabel(labelId)
        cache(labelMusicBrainzModel)
        return lookupLabel(labelId)
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
