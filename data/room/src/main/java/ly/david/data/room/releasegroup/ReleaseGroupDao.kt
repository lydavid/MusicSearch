package ly.david.data.room.releasegroup

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.coverart.UpdateReleaseGroupCoverArtDao
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.room.BaseDao
import ly.david.data.room.artist.credit.ArtistCreditDao

@Dao
abstract class ReleaseGroupDao : BaseDao<ReleaseGroupRoomModel>(), ArtistCreditDao, UpdateReleaseGroupCoverArtDao {

    @Transaction
    open suspend fun insertAllReleaseGroupsWithArtistCredits(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
        releaseGroups.forEach { releaseGroup ->
            insertReleaseGroupWithArtistCredits(releaseGroup)
        }
    }

    @Transaction
    open suspend fun insertReleaseGroupWithArtistCredits(releaseGroup: ReleaseGroupMusicBrainzModel) {
        insertArtistCredits(artistCredits = releaseGroup.artistCredits, resourceId = releaseGroup.id)
        insert(releaseGroup.toRoomModel())
    }

    // Lookup
    @Transaction
    @Query("SELECT * FROM release_group WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroup(releaseGroupId: String): ReleaseGroupWithAllData?

    @Query(
        """
            UPDATE release_group
            SET cover_art_path = :coverArtPath
            WHERE id = :releaseGroupId
        """
    )
    abstract override suspend fun setReleaseGroupCoverArtPath(releaseGroupId: String, coverArtPath: String)
}
