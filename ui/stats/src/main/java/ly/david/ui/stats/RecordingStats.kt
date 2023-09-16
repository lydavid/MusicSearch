package ly.david.ui.stats

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RoomRelationDao

/**
 * Stats about recordings.
 */
interface RecordingStats {
    val relationDao: RoomRelationDao

    suspend fun getTotalRemoteRecordings(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RECORDING)?.remoteCount

    suspend fun getTotalLocalRecordings(entityId: String): Int
}
