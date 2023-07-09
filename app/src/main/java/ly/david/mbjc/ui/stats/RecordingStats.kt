package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationDao

/**
 * Stats about recordings.
 */
interface RecordingStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteRecordings(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RECORDING)?.remoteCount

    suspend fun getTotalLocalRecordings(entityId: String): Int
}
