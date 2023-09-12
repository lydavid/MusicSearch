package ly.david.data.domain.label

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.label.LabelDao
import ly.david.data.room.label.toLabelRoomModel
import org.koin.core.annotation.Single

@Single
class LabelRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupLabel(labelId: String): LabelScaffoldModel {
        val labelWithAllData = labelDao.getLabel(labelId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(labelId)
        if (labelWithAllData != null && hasUrlsBeenSavedForEntity) {
            return labelWithAllData.toLabelScaffoldModel()
        }

        val labelMusicBrainzModel = musicBrainzApi.lookupLabel(labelId)
        labelDao.withTransaction {
            labelDao.insert(labelMusicBrainzModel.toLabelRoomModel())
            relationRepository.insertAllRelations(
                entityId = labelId,
                relationMusicBrainzModels = labelMusicBrainzModel.relations,
            )
        }
        return lookupLabel(labelId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupLabel(
            labelId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
