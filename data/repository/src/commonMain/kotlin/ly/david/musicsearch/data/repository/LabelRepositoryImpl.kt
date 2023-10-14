package ly.david.musicsearch.data.repository

import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.label.LabelScaffoldModel
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.label.LabelRepository
import ly.david.musicsearch.domain.relation.RelationRepository

class LabelRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
) : LabelRepository {

    override suspend fun lookupLabel(labelId: String): LabelScaffoldModel {
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
