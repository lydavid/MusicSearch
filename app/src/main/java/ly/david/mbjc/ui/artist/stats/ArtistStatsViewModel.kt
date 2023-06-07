package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroupDao
import ly.david.data.room.artist.releases.ArtistReleaseDao
import ly.david.data.room.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleaseGroupsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
    override val relationDao: RelationDao
) : ViewModel(), ReleaseGroupsStats, ReleasesStats, RelationsStats {

    override suspend fun getTotalLocalReleaseGroups(resourceId: String) =
        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(resourceId)

    override suspend fun getCountOfEachAlbumType(resourceId: String) =
        artistReleaseGroupDao.getCountOfEachAlbumType(resourceId)

    override suspend fun getTotalLocalReleases(resourceId: String): Int {
        return artistReleaseDao.getNumberOfReleasesByArtist(resourceId)
    }
}
