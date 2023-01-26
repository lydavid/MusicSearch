package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationDao

/**
 * Stats about places.
 */
interface PlacesStats {
    val relationDao: RelationDao

    suspend fun getTotalRemotePlaces(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.PLACE)?.remoteCount

    suspend fun getTotalLocalPlaces(resourceId: String): Int
}
