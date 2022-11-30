package ly.david.data.persistence.artist.credit

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.getDisplayNames
import ly.david.data.network.ArtistCreditMusicBrainzModel
import ly.david.data.persistence.INSERTION_FAILED_DUE_TO_CONFLICT

/**
 * Needs to be implemented by any Dao that wishes to interface with artist credits.
 *
 * This will be implemented multiple times, but at least we don't have to copy/paste it ourselves.
 */
interface ArtistCreditDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCredit(artistCredit: ArtistCredit): Long

    @Query("SELECT * FROM artist_credits WHERE name = :name")
    suspend fun getArtistCreditByName(name: String): ArtistCredit

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArtistCreditNames(artistCreditNameRoomModel: List<ArtistCreditNameRoomModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCreditResource(artistCreditResource: ArtistCreditResource): Long

    suspend fun insertArtistCredits(
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
        resourceId: String
    ) {
        val artistCreditName = artistCredits.getDisplayNames()
        var artistCreditId = insertArtistCredit(ArtistCredit(name = artistCreditName))
        if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
            artistCreditId = getArtistCreditByName(artistCreditName).id
        } else {
            insertAllArtistCreditNames(artistCredits.toRoomModels(artistCreditId))
        }

        insertArtistCreditResource(
            ArtistCreditResource(
                artistCreditId = artistCreditId,
                resourceId = resourceId
            )
        )
    }
}
