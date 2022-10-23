package ly.david.data.persistence.release

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.area.ReleaseCountry

@Dao
abstract class ReleaseDao : BaseDao<ReleaseRoomModel> {

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): ReleaseRoomModel?

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllReleaseEvents(releaseCountries: List<ReleaseCountry>)
}
