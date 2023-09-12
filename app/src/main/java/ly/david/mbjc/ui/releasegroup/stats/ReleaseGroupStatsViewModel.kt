package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupStatsViewModel(
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    override val relationDao: RelationDao,
) : ViewModel(),
    RelationsStats,
    ReleasesStats {

    override suspend fun getTotalLocalReleases(entityId: String) =
        releaseReleaseGroupDao.getNumberOfReleasesByReleaseGroup(entityId)
}
