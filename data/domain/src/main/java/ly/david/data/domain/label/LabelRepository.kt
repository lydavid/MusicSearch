package ly.david.data.domain.label

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.LabelDao
import org.koin.core.annotation.Single

@Single
class LabelRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupLabel(labelId: String): LabelScaffoldModel {
        val label = labelDao.getLabel(labelId)
        val urlRelations = relationRepository.getEntityUrlRelationships(labelId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(labelId)
        if (label != null && hasUrlsBeenSavedForEntity) {
            return label.toLabelScaffoldModel(urlRelations)
        }

        val labelMusicBrainzModel = musicBrainzApi.lookupLabel(labelId)
        cache(labelMusicBrainzModel)
        return lookupLabel(labelId)
    }

    private fun cache(label: LabelMusicBrainzModel) {
        labelDao.withTransaction {
            labelDao.insert(label)
            relationRepository.insertAllUrlRelations(
                entityId = label.id,
                relationMusicBrainzModels = label.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupLabel(
            labelId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
