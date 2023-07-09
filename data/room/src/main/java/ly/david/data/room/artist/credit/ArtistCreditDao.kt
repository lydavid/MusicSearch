package ly.david.data.room.artist.credit

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.getDisplayNames
import ly.david.data.network.ArtistCreditMusicBrainzModel
import ly.david.data.room.INSERTION_FAILED_DUE_TO_CONFLICT

/**
 * Needs to be implemented by any Dao that wishes to interface with artist credits.
 *
 * This will be implemented multiple times, but at least we don't have to copy/paste it ourselves.
 */
interface ArtistCreditDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCredit(artistCredit: ArtistCredit): Long

    @Query("SELECT * FROM artist_credit WHERE name = :name")
    suspend fun getArtistCreditByName(name: String): ArtistCredit

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArtistCreditNames(artistCreditNameRoomModel: List<ArtistCreditNameRoomModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCreditEntityLink(artistCreditEntityLink: ArtistCreditEntityLink): Long

    suspend fun insertArtistCredits(
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
        entityId: String
    ) {
        val artistCreditName = artistCredits.getDisplayNames()
        var artistCreditId = insertArtistCredit(ArtistCredit(name = artistCreditName))
        if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
            artistCreditId = getArtistCreditByName(artistCreditName).id
        } else {
            insertAllArtistCreditNames(artistCredits.toArtistCreditNameRoomModels(artistCreditId))
        }

        insertArtistCreditEntityLink(
            ArtistCreditEntityLink(
                artistCreditId = artistCreditId,
                entityId = entityId
            )
        )
    }
}
