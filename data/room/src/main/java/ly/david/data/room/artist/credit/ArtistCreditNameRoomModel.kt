package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.ArtistCreditName
import ly.david.data.network.ArtistCreditMusicBrainzModel

/**
 * Represents a single name in an [ArtistCredit].
 */
@Entity(
    tableName = "artist_credit_name",
    primaryKeys = ["artist_credit_id", "position"],
    foreignKeys = [
        ForeignKey(
            entity = ArtistCredit::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("artist_credit_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArtistCreditNameRoomModel(
    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long,

    @ColumnInfo(name = "position")
    val position: Int,

    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String? = null,
) : ArtistCreditName

/**
 * Converts artist credits to an appropriate model to store.
 *
 * We take in [artistCreditId] so that we don't duplicate [ArtistCreditNameRoomModel].
 *
 * The receiver must be a list because we need its index.
 */
internal fun List<ArtistCreditMusicBrainzModel>?.toArtistCreditNameRoomModels(
    artistCreditId: Long,
): List<ArtistCreditNameRoomModel> =
    this?.mapIndexed { index, artistCredit ->
        ArtistCreditNameRoomModel(
            artistCreditId = artistCreditId,
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase,
            position = index
        )
    }.orEmpty()
