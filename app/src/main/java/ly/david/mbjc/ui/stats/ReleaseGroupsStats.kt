package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount

/**
 * Stats about release groups.
 */
internal interface ReleaseGroupsStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteReleaseGroups(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.remoteCount

    suspend fun getTotalLocalReleaseGroups(entityId: String): Int

    suspend fun getCountOfEachAlbumType(entityId: String): List<ReleaseGroupTypeCount>
}
