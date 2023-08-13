package ly.david.data.domain.label

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.label.LabelDao
import ly.david.data.room.label.toLabelRoomModel

@Singleton
class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupLabel(labelId: String): LabelScaffoldModel {
        val labelRoomModel = labelDao.getLabel(labelId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(labelId)
        if (labelRoomModel != null && hasUrlsBeenSavedForEntity) {
            return labelRoomModel.toLabelScaffoldModel()
        }

        val labelMusicBrainzModel = musicBrainzApiService.lookupLabel(labelId)
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
        return musicBrainzApiService.lookupLabel(
            labelId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
