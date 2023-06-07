package ly.david.mbjc.ui.stats

import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount

/**
 * Stats about release groups.
 */
internal interface ReleaseGroupsStats {
    val relationDao: RelationDao

    suspend fun getTotalRemoteReleaseGroups(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount

    suspend fun getTotalLocalReleaseGroups(resourceId: String): Int

    suspend fun getCountOfEachAlbumType(resourceId: String): List<ReleaseGroupTypeCount>
}
