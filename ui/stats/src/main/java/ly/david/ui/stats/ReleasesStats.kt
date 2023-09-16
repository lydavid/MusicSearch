package ly.david.ui.stats

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RoomRelationDao

/**
 * Stats about releases.
 */
interface ReleasesStats {
    val relationDao: RoomRelationDao

    suspend fun getTotalRemoteReleases(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.remoteCount

    suspend fun getTotalLocalReleases(entityId: String): Int
}
