package ly.david.data.persistence.releasegroup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.artist.credit.ArtistCreditDao

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
        insertArtistCredits(artistCredits = releaseGroup.artistCredits, resourceId = releaseGroup.id)
        insert(releaseGroup.toReleaseGroupRoomModel())
    }

    // Lookup
    @Transaction
    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroupWithArtistCredits(releaseGroupId: String): ReleaseGroupWithArtistCredits?
}
