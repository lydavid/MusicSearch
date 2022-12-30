package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.releasegroup.ReleaseReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class ReleaseGroupStatsViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(releaseGroupId: String) =
        relationDao.getBrowseResourceCount(releaseGroupId, MusicBrainzResource.RELEASE)?.remoteCount ?: 0

    suspend fun getNumberOfReleasesByReleaseGroup(releaseGroupId: String) =
        releaseReleaseGroupDao.getNumberOfReleasesByReleaseGroup(releaseGroupId)
}
