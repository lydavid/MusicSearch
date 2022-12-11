package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.WorkListItemModel
import ly.david.data.domain.toWorkListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.WORK_INC_DEFAULT
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.work.WorkDao
import ly.david.data.persistence.work.toWorkRoomModel

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
        return workMusicBrainzModel.toWorkListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupWork(
            workId = resourceId,
            include = WORK_INC_DEFAULT
        ).relations
    }
}
