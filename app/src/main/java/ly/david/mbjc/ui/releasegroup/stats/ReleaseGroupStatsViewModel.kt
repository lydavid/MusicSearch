package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.releasegroup.releases.RoomReleaseReleaseGroupDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupStatsViewModel(
    private val releaseReleaseGroupDao: RoomReleaseReleaseGroupDao,
    override val relationDao: RoomRelationDao,
) : ViewModel(),
    RelationsStats,
    ReleasesStats {

    override suspend fun getTotalLocalReleases(entityId: String) =
        releaseReleaseGroupDao.getNumberOfReleasesByReleaseGroup(entityId)
}
