package ly.david.data.persistence.release

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class ReleaseDao : BaseDao<ReleaseRoomModel> {

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): ReleaseRoomModel?

    @Transaction
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getReleaseWithAllData(releaseId: String): ReleaseWithAllData?

    @Query(
        """
        DELETE FROM releases WHERE id = :releaseId
        """
    )
    abstract suspend fun deleteReleaseById(releaseId: String)

    @Query(
        """
            UPDATE releases
            SET cover_art_url = :coverArtUrl
            WHERE id = :releaseId
        """
    )
    abstract suspend fun setReleaseCoverArtUrl(releaseId: String, coverArtUrl: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllArtistCredits(artistCredits: List<ReleaseArtistCreditRoomModel>)

    @Transaction
    @Query(
        """
        SELECT ra.*
        FROM releases r
        INNER JOIN releases_artists ra ON r.id = ra.release_id
        where r.id = :releaseId
        ORDER BY ra.`order`
    """
    )
    abstract suspend fun getReleaseArtistCredits(releaseId: String): List<ReleaseArtistCreditRoomModel>
}
