package ly.david.ui.stats

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RoomRelationDao

/**
 * Stats about events.
 */
interface EventStats {
    val relationDao: RoomRelationDao

    suspend fun getTotalRemoteEvents(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.EVENT)?.remoteCount

    suspend fun getTotalLocalEvents(entityId: String): Int
}
