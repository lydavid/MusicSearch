package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroupDao
import ly.david.data.room.artist.releases.ArtistReleaseDao
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleaseGroupsStats
import ly.david.ui.stats.ReleasesStats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
    override val relationDao: RelationDao,
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
