package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.relation.RelationDao

/**
 * Stats about events.
 */
interface EventStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteEvents(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.EVENT)?.remoteCount

    suspend fun getTotalLocalEvents(resourceId: String): Int
}
