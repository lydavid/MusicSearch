package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.WorkListItemModel
import ly.david.data.domain.toWorkListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.WORK_INC_DEFAULT
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.recording.toRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.persistence.work.RecordingWork
import ly.david.data.persistence.work.RecordingsWorksDao
import ly.david.data.persistence.work.WorkDao
import ly.david.data.persistence.work.toWorkRoomModel

@Singleton
class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val recordingsWorksDao: RecordingsWorksDao,
    private val workDao: WorkDao,
    private val relationDao: RelationDao,
) : RelationsListRepository, RecordingsListRepository {

    suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean = false,
        hasRelationsBeenStored: suspend () -> Boolean,
        markResourceHasRelations: suspend () -> Unit
    ): WorkListItemModel {
        val workRoomModel = workDao.getWork(workId)
        if (!forceRefresh && workRoomModel != null && hasRelationsBeenStored()) {
            return workRoomModel.toWorkListItemModel()
        }

        val workMusicBrainzModel = musicBrainzApiService.lookupWork(
            workId = workId,
            include = WORK_INC_DEFAULT
        )
        val relations = mutableListOf<RelationRoomModel>()
        workMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = workId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)
        markResourceHasRelations()

        workDao.insert(workMusicBrainzModel.toWorkRoomModel())
        return workMusicBrainzModel.toWorkListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupWork(
            workId = resourceId,
            include = WORK_INC_DEFAULT
        ).relations
    }

    override suspend fun browseRecordingsAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseRecordingsByWork(
            workId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RECORDING,
                    localCount = response.recordings.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.RECORDING, response.recordings.size)
        }

        val recordingMusicBrainzModels = response.recordings
        recordingDao.insertAll(recordingMusicBrainzModels.map { it.toRoomModel() })
        recordingsWorksDao.insertAll(
            recordingMusicBrainzModels.map { recording ->
                RecordingWork(
                    recordingId = recording.id,
                    workId = resourceId
                )
            }
        )

        return recordingMusicBrainzModels.size
    }

    override suspend fun getRemoteRecordingsCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.remoteCount

    override suspend fun getLocalRecordingsCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.localCount ?: 0

    override suspend fun deleteRecordingsByResource(resourceId: String) {
        recordingsWorksDao.deleteRecordingsByWork(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RECORDING)
    }

    override fun getRecordingsPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, RecordingForListItem> = when {
        query.isEmpty() -> {
            recordingsWorksDao.getRecordingsByWork(resourceId)
        }
        else -> {
            recordingsWorksDao.getRecordingsByWorkFiltered(
                workId = resourceId,
                query = "%$query%"
            )
        }
    }
}
