package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single unique artist credit made up of many [ArtistCreditNameRoomModel].
 *
 * We store the fully formatted [name] so that we can quickly compare to ensure we don't insert duplicates.
 */
@Entity(
    tableName = "artist_credit",
    indices = [Index(value = ["name"], unique = true)]
)
data class ArtistCredit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    // The full artist credit name, with all artist names and join phrases combined
    @ColumnInfo(name = "name")
    val name: String,
)
