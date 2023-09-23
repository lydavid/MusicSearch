package ly.david.mbjc.ui.releasegroup.releases

import androidx.paging.PagingSource
import ly.david.data.core.ReleaseForListItem
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByReleaseGroupViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: ReleasesPagedList,
    releaseDao: ReleaseDao,
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
            releaseGroupId = entityId,
            releaseIds = releaseMusicBrainzModels.map { release -> release.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseReleaseGroupDao.withTransaction {
            releaseReleaseGroupDao.deleteReleasesByReleaseGroup(entityId)
            // TODO: do we need this?
//            releaseReleaseGroupDao.deleteReleaseReleaseGroupLinks(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> =
        releaseReleaseGroupDao.getReleasesByReleaseGroup(
            releaseGroupId = entityId,
            query = "%$query%"
        )
}
