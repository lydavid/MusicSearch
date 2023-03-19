package ly.david.mbjc.ui.recording.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.RecordingRelease
import ly.david.data.persistence.recording.RecordingReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.mbjc.ui.common.ReleasesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class ReleasesByRecordingViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingReleaseDao: RecordingReleaseDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApiService.browseReleasesByRecording(
            recordingId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    ) {
        recordingReleaseDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                RecordingRelease(
                    releaseId = release.id,
                    recordingId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        recordingReleaseDao.withTransaction {
            recordingReleaseDao.deleteReleasesByRecording(resourceId)
            recordingReleaseDao.deleteRecordingReleaseLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            recordingReleaseDao.getReleasesByRecording(resourceId)
        }
        else -> {
            recordingReleaseDao.getReleasesByRecordingFiltered(
                recordingId = resourceId,
                query = "%$query%"
            )
        }
    }
}
