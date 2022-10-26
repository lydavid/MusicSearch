package ly.david.mbjc.ui.releasegroup.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class ReleaseGroupStatsViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(releaseGroupId: String) =
        relationDao.getBrowseResourceOffset(releaseGroupId, MusicBrainzResource.RELEASE)?.remoteCount ?: 0

    suspend fun getNumberOfReleasesInReleaseGroup(releaseGroupId: String) =
        releasesReleaseGroupsDao.getNumberOfReleasesInReleaseGroup(releaseGroupId)
}
