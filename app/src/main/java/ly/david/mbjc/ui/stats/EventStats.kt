package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationDao

/**
 * Stats about events.
 */
interface EventStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteEvents(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.EVENT)?.remoteCount

    suspend fun getTotalLocalEvents(entityId: String): Int
}
