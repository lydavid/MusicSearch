package ly.david.mbjc.ui.artist.releasegroups

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityViewModel
import ly.david.ui.common.releasegroup.ReleaseGroupsPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupsByArtistViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    releaseGroupDao: ReleaseGroupDao,
    releaseGroupsPagedList: ReleaseGroupsPagedList,
) : ReleaseGroupsByEntityViewModel(
    browseEntityCountDao = browseEntityCountDao,
    releaseGroupDao = releaseGroupDao,
    releaseGroupsPagedList = releaseGroupsPagedList,
) {

    override suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse {
        return musicBrainzApi.browseReleaseGroupsByArtist(
            artistId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ) {
        artistReleaseGroupDao.insertAll(
            artistId = entityId,
            releaseGroupIds = releaseGroupMusicBrainzModels.map { releaseGroup -> releaseGroup.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        artistReleaseGroupDao.withTransaction {
            artistReleaseGroupDao.deleteReleaseGroupsByArtist(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE_GROUP)
        }
    }

    override fun getLinkedEntitiesPagingSource(entityId: String, query: String, sorted: Boolean) =
        artistReleaseGroupDao.getReleaseGroupsByArtist(
            artistId = entityId,
            query = "%$query%",
            sorted = sorted
        )
}
