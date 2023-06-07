package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.relation.RelationDao

/**
 * Stats about recordings.
 */
interface RecordingStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteRecordings(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.remoteCount

    suspend fun getTotalLocalRecordings(resourceId: String): Int
}
