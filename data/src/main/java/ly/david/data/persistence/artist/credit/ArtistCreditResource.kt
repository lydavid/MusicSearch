package ly.david.data.persistence.artist.credit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.persistence.recording.RecordingRoomModel
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel

/**
 * Junction table between [ArtistCredit] and one of:
 * - [ReleaseGroupRoomModel]
 * - [ReleaseRoomModel]
 * - [RecordingRoomModel]
 */
@Entity(
    tableName = "artist_credit_resource",
    primaryKeys = ["artist_credit_id", "resource_id"],
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
data class ArtistCreditResource(
    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long,

    @ColumnInfo(name = "resource_id")
    val resourceId: String
)
