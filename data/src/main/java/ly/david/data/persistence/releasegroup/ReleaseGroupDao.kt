package ly.david.data.persistence.releasegroup

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.artist.credit.ArtistCreditDao

@Dao
abstract class ReleaseGroupDao : BaseDao<ReleaseGroupRoomModel>(), ArtistCreditDao {

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
    abstract suspend fun getReleaseGroupWithArtistCredits(releaseGroupId: String): ReleaseGroupWithArtistCredits?

    @Query(
        """
            UPDATE release_group
            SET cover_art_url = :coverArtUrl
            WHERE id = :releaseGroupId
        """
    )
    abstract suspend fun setReleaseGroupCoverArtUrl(releaseGroupId: String, coverArtUrl: String)

    @Query(
        """
            UPDATE release_group
            SET has_cover_art = :hasCoverArt
            WHERE id = :releaseGroupId
        """
    )
    abstract suspend fun setReleaseGroupHasCoverArt(releaseGroupId: String, hasCoverArt: Boolean)
}
