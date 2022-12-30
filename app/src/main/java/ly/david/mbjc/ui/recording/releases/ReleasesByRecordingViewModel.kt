package ly.david.mbjc.ui.recording.releases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.ReleaseRecording
import ly.david.data.persistence.recording.ReleasesRecordingsDao
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.data.persistence.release.toRoomModel
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.common.paging.PagedListImpl

@HiltViewModel
internal class ReleasesByRecordingViewModel @Inject constructor(
    private val pagedListImpl: PagedListImpl<ReleaseForListItem, ReleaseListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
    private val releasesRecordingsDao: ReleasesRecordingsDao
) : ViewModel(),
    PagedList<ReleaseListItemModel> by pagedListImpl,
    BrowseResourceUseCase<ReleaseForListItem, ReleaseListItemModel> {

    init {
        pagedListImpl.scope = viewModelScope
        pagedListImpl.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByRecording(
            recordingId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val releaseMusicBrainzModels = response.releases
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toRoomModel() })
        releasesRecordingsDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseRecording(
                    releaseId = release.id,
                    recordingId = resourceId
                )
            }
        )

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        releasesRecordingsDao.withTransaction {
            releasesRecordingsDao.deleteReleasesByRecording(resourceId)
            releasesRecordingsDao.deleteRecordingReleaseLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releasesRecordingsDao.getReleasesByRecording(resourceId)
        }
        else -> {
            releasesRecordingsDao.getReleasesByRecordingFiltered(
                recordingId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: ReleaseForListItem): ReleaseListItemModel {
        return roomModel.toReleaseListItemModel()
    }
}
