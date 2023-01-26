package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.release.stats.ReleaseStatsViewModel

/**
 * Stats about releases.
 * Not to be confused with [ReleaseStatsViewModel] which holds logic for the stats screen for a particular release.
 */
interface ReleasesStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteReleases(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    suspend fun getTotalLocalReleases(resourceId: String): Int
}
