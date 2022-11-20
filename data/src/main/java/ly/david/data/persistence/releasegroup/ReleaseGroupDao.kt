package ly.david.data.persistence.releasegroup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.getDisplayNames
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.data.persistence.artist.ArtistCreditDao
import ly.david.data.persistence.artist.ArtistCreditResource
import ly.david.data.persistence.artist.ArtistCreditRoomModel
import ly.david.data.persistence.artist.toRoomModels

@Dao
abstract class ReleaseGroupDao : BaseDao<ReleaseGroupRoomModel>, ArtistCreditDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllReleaseGroupsWithArtistCredits(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
        releaseGroups.forEach { releaseGroup ->
            insertReleaseGroupWithArtistCredits(releaseGroup)
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReleaseGroupWithArtistCredits(releaseGroup: ReleaseGroupMusicBrainzModel) {
        val artistCreditName = releaseGroup.artistCredits.getDisplayNames()
        var artistCreditId = insertArtistCredit(ArtistCreditRoomModel(name = artistCreditName))
        if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
            artistCreditId = getArtistCreditByName(artistCreditName).id
        } else {
            insertAllArtistCreditNames(releaseGroup.artistCredits.toRoomModels(artistCreditId))
        }

        insertArtistCreditResource(
            ArtistCreditResource(
                artistCreditId = artistCreditId,
                resourceId = releaseGroup.id
            )
        )

        insert(releaseGroup.toReleaseGroupRoomModel())
    }

    // Lookup
    @Transaction
    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroupWithArtistCredits(releaseGroupId: String): ReleaseGroupWithArtistCredits?
}
