package ly.david.mbjc.ui.work.recordings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.toRecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.recording.toRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.work.RecordingWork
import ly.david.data.persistence.work.RecordingsWorksDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.common.paging.PagedListImpl

@HiltViewModel
internal class RecordingsByWorkViewModel @Inject constructor(
    private val pagedListImpl: PagedListImpl<RecordingForListItem, RecordingListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationDao: RelationDao,
    private val recordingsWorksDao: RecordingsWorksDao,
) : ViewModel(),
    PagedList<RecordingListItemModel> by pagedListImpl,
    BrowseResourceUseCase<RecordingForListItem, RecordingListItemModel> {

    init {
        pagedListImpl.scope = viewModelScope
        pagedListImpl.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
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
            relationDao.incrementLocalCountForResource(
                resourceId,
                MusicBrainzResource.RECORDING,
                response.recordings.size
            )
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

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        recordingsWorksDao.deleteRecordingsByWork(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RECORDING)
    }

    override fun getLinkedResourcesPagingSource(
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

    override fun transformRoomToListItemModel(roomModel: RecordingForListItem): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
