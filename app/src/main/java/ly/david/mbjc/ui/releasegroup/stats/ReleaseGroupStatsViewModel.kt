package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao

@HiltViewModel
internal class ReleaseGroupStatsViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseDao: ReleaseDao
) : ViewModel() {

    suspend fun getTotalReleases(releaseGroupId: String) =
        releaseGroupDao.getReleaseGroup(releaseGroupId)?.releaseCount ?: 0

    suspend fun getNumberOfReleasesInReleaseGroup(releaseGroupId: String) =
        releaseDao.getNumberOfReleasesInReleaseGroup(releaseGroupId)
}
