package ly.david.data.persistence.artist

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Needs to be implemented by any Dao that wishes to interface with artist credits.
 *
 * This will be implemented multiple times, but at least we don't have to copy/paste it ourselves.
 */
interface ArtistCreditDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCredit(artistCredit: ArtistCreditRoomModel): Long

    @Query(
        """
            SELECT *
            FROM artist_credits
            WHERE name = :name
        """
    )
    suspend fun getArtistCreditByName(name: String): ArtistCreditRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArtistCreditNames(artistCreditNameRoomModel: List<ArtistCreditNameRoomModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCreditResource(artistCreditResource: ArtistCreditResource): Long
}
