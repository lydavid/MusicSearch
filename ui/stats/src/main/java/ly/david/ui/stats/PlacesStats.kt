package ly.david.ui.stats

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RoomRelationDao

/**
 * Stats about places.
 */
interface PlacesStats {
    val relationDao: RoomRelationDao

    suspend fun getTotalRemotePlaces(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.PLACE)?.remoteCount

    suspend fun getTotalLocalPlaces(entityId: String): Int
}
