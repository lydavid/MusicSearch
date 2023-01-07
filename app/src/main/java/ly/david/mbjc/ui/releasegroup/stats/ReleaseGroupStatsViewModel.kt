package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.releasegroup.ReleaseReleaseGroupDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class ReleaseGroupStatsViewModel @Inject constructor(
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats, ReleasesStats {

    override suspend fun getTotalLocalReleases(resourceId: String) =
        releaseReleaseGroupDao.getNumberOfReleasesByReleaseGroup(resourceId)
}
