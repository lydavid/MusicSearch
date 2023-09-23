package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.artist.releasegroups.RoomArtistReleaseGroupDao
import ly.david.data.room.artist.releases.RoomArtistReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleaseGroupsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArtistStatsViewModel(
    private val artistReleaseGroupDao: RoomArtistReleaseGroupDao,
    private val artistReleaseDao: RoomArtistReleaseDao,
    override val relationDao: RoomRelationDao,
) : ViewModel(),
    ReleaseGroupsStats,
    ReleasesStats,
    RelationsStats {

    override suspend fun getTotalLocalReleaseGroups(entityId: String) =
        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(entityId)

    override suspend fun getCountOfEachAlbumType(entityId: String) =
        artistReleaseGroupDao.getCountOfEachAlbumType(entityId)

    override suspend fun getTotalLocalReleases(entityId: String): Int {
        return artistReleaseDao.getNumberOfReleasesByArtist(entityId)
    }
}
