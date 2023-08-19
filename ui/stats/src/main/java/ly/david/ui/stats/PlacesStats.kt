package ly.david.ui.stats

import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationDao

/**
 * Stats about places.
 */
interface PlacesStats {
    val relationDao: RelationDao

    suspend fun getTotalRemotePlaces(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.PLACE)?.remoteCount

    suspend fun getTotalLocalPlaces(entityId: String): Int
}
