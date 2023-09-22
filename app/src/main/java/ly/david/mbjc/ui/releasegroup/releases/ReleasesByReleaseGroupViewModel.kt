package ly.david.mbjc.ui.releasegroup.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.release.RoomReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.releasegroup.releases.RoomReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByReleaseGroupViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseReleaseGroupDao: RoomReleaseReleaseGroupDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: ReleasesPagedList,
    releaseDao: RoomReleaseDao,
) : ReleasesByEntityViewModel(
    browseEntityCountDao = browseEntityCountDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByReleaseGroup(
            releaseGroupId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        releaseReleaseGroupDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseReleaseGroup(
                    releaseId = release.id,
                    releaseGroupId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseReleaseGroupDao.withTransaction {
            releaseReleaseGroupDao.deleteReleasesByReleaseGroup(entityId)
            releaseReleaseGroupDao.deleteReleaseReleaseGroupLinks(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroup(entityId)
        }
        else -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = entityId,
                query = "%$query%"
            )
        }
    }
}
