package ly.david.ui.stats

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.releasegroup.RoomReleaseGroupTypeCount

/**
 * Stats about release groups.
 */
interface ReleaseGroupsStats {
    val relationDao: RoomRelationDao

    suspend fun getTotalRemoteReleaseGroups(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.remoteCount

    suspend fun getTotalLocalReleaseGroups(entityId: String): Int

    suspend fun getCountOfEachAlbumType(entityId: String): List<RoomReleaseGroupTypeCount>
}
