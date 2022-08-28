package ly.david.mbjc.ui.work

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Work
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel
import ly.david.mbjc.data.persistence.toWorkRoomModel
import ly.david.mbjc.data.persistence.work.WorkDao

@Singleton
internal class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val workDao: WorkDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var work: Work? = null

    suspend fun lookupWork(workId: String): Work =
        work ?: run {

            // Use cached model.
            val workRoomModel = workDao.getWork(workId)
            if (workRoomModel != null) {
                incrementOrInsertLookupHistory(workRoomModel)
                return workRoomModel
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
            workMusicBrainzModel
        }

    private suspend fun incrementOrInsertLookupHistory(work: Work) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = work.name,
                resource = MusicBrainzResource.WORK,
                mbid = work.id
            )
        )
    }
}
