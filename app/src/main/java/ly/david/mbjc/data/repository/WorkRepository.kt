package ly.david.mbjc.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Work
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.api.MusicBrainzApiService
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.data.persistence.work.WorkDao
import ly.david.mbjc.data.persistence.work.toWorkRoomModel

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
                title = work.name,
                resource = MusicBrainzResource.WORK,
                mbid = work.id
            )
        )
    }
}
