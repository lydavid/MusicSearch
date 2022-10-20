package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Work
import ly.david.data.domain.WorkUiModel
import ly.david.data.domain.toWorkUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.persistence.work.WorkDao
import ly.david.data.persistence.work.toWorkRoomModel

@Singleton
class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val workDao: WorkDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var work: WorkUiModel? = null

    suspend fun lookupWork(workId: String): WorkUiModel =
        work ?: run {

            // Use cached model.
            val workRoomModel = workDao.getWork(workId)
            if (workRoomModel != null) {
                incrementOrInsertLookupHistory(workRoomModel)
                return workRoomModel.toWorkUiModel()
            }

            val workMusicBrainzModel = musicBrainzApiService.lookupWork(workId)
            workDao.insert(workMusicBrainzModel.toWorkRoomModel())

            val workRelations = mutableListOf<RelationRoomModel>()
            workMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = workId,
                    order = index
                )?.let { relationRoomModel ->
                    workRelations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(workRelations)

            incrementOrInsertLookupHistory(workMusicBrainzModel)
            workMusicBrainzModel.toWorkUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(work: Work) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = work.name,
                resource = MusicBrainzResource.WORK,
                mbid = work.id
            )
        )
    }
}
