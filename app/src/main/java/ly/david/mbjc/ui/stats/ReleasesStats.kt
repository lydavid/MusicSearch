package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationDao
import ly.david.mbjc.ui.release.stats.ReleaseStatsViewModel

/**
 * Stats about releases.
 * Not to be confused with [ReleaseStatsViewModel] which holds logic for the stats screen for a particular release.
 */
interface ReleasesStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteReleases(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.remoteCount

    suspend fun getTotalLocalReleases(entityId: String): Int
}
