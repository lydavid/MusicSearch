package ly.david.mbjc.ui.artist.releases

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByArtistViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistReleaseDao: ArtistReleaseDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    releaseDao: ReleaseDao,
    pagedList: ReleasesPagedList,
) : ReleasesByEntityViewModel(
    browseEntityCountDao = browseEntityCountDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByArtist(
            artistId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        artistReleaseDao.insertAll(
            artistId = entityId,
            releaseIds = releaseMusicBrainzModels.map { release -> release.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        artistReleaseDao.withTransaction {
            artistReleaseDao.deleteReleasesByArtist(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> =
        artistReleaseDao.getReleasesByArtist(
            artistId = entityId,
            query = "%$query%",
        )
}
