package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.room.recording.RecordingRoomModel
import ly.david.data.room.release.ReleaseRoomModel
import ly.david.data.room.releasegroup.ReleaseGroupRoomModel

/**
 * Junction table between [ArtistCredit] and one of:
 * - [ReleaseGroupRoomModel]
 * - [ReleaseRoomModel]
 * - [RecordingRoomModel]
 */
@Entity(
    tableName = "artist_credit_entity",
    primaryKeys = ["artist_credit_id", "entity_id"],
    // TODO: can't FK to multiple tables directly?
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
data class ArtistCreditEntityLink(
    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long,

    @ColumnInfo(name = "entity_id")
    val entityId: String
)
