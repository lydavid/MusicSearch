package ly.david.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.ArtistCredit

/**
 * An artist's credit for a [Recording].
 * A recording can have many artists credits. It can even have the same artist listed twice under different names.
 *
 * An [ArtistCredit] for a [Recording] should map to this.
 */
@Entity(
    tableName = "recordings_artists",
    primaryKeys = ["recording_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = RecordingRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recording_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecordingArtistCreditRoomModel(
    @ColumnInfo(name = "recording_id")
    val recordingId: String,

    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String? = null,

    @ColumnInfo(name = "order")
    val order: Int
) : ArtistCredit
