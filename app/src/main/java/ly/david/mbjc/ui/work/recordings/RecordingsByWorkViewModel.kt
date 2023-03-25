package ly.david.mbjc.ui.work.recordings

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.work.RecordingWork
import ly.david.data.persistence.work.RecordingWorkDao
import ly.david.mbjc.ui.common.RecordingsByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class RecordingsByWorkViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingWorkDao: RecordingWorkDao,
    private val relationDao: RelationDao,
    recordingDao: RecordingDao,
    pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
) : RecordingsByEntityViewModel(
    relationDao = relationDao,
    recordingDao = recordingDao,
    pagedList = pagedList
) {

    override suspend fun browseRecordingsByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApiService.browseRecordingsByWork(
            workId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        recordingMusicBrainzModels: List<RecordingMusicBrainzModel>
    ) {
        recordingWorkDao.insertAll(
            recordingMusicBrainzModels.map { recording ->
                RecordingWork(
                    recordingId = recording.id,
                    workId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        recordingWorkDao.deleteRecordingsByWork(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RECORDING)
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, RecordingForListItem> = when {
        query.isEmpty() -> {
            recordingWorkDao.getRecordingsByWork(resourceId)
        }
        else -> {
            recordingWorkDao.getRecordingsByWorkFiltered(
                workId = resourceId,
                query = "%$query%"
            )
        }
    }
}
