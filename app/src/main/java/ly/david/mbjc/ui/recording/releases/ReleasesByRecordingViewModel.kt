package ly.david.mbjc.ui.recording.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.recording.releases.RecordingRelease
import ly.david.data.room.recording.releases.RecordingReleaseDao
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.paging.PagedList
import ly.david.ui.common.release.ReleasesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByRecordingViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingReleaseDao: RecordingReleaseDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByRecording(
            recordingId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
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

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        recordingReleaseDao.withTransaction {
            recordingReleaseDao.deleteReleasesByRecording(entityId)
            recordingReleaseDao.deleteRecordingReleaseLinks(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            recordingReleaseDao.getReleasesByRecording(entityId)
        }
        else -> {
            recordingReleaseDao.getReleasesByRecordingFiltered(
                recordingId = entityId,
                query = "%$query%"
            )
        }
    }
}
