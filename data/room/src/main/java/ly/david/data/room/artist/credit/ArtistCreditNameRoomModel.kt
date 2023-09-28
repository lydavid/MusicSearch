package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.core.ArtistCreditName

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
