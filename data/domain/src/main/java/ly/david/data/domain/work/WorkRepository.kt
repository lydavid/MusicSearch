package ly.david.data.domain.work

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.data.domain.listitem.toWorkListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.WORK_INC_DEFAULT
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.work.WorkDao
import ly.david.data.room.work.toWorkAttributeRoomModel
import ly.david.data.room.work.toWorkRoomModel

@Singleton
class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val workDao: WorkDao,
) : RelationsListRepository {

    suspend fun lookupWork(
        workId: String,
    ): WorkListItemModel {
        val workRoomModel = workDao.getWork(workId)
        if (workRoomModel != null) {
            return workRoomModel.toWorkListItemModel()
        }

        val workMusicBrainzModel = musicBrainzApiService.lookupWork(workId = workId)
        workDao.insert(workMusicBrainzModel.toWorkRoomModel())
        workDao.insertAllAttributes(
            workMusicBrainzModel.attributes?.map { it.toWorkAttributeRoomModel(workId) }.orEmpty()
        )

        return workMusicBrainzModel.toWorkListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupWork(
            workId = entityId,
            include = WORK_INC_DEFAULT
        ).relations
    }
}
