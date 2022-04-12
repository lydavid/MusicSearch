package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Track
import ly.david.mbjc.data.network.MusicBrainzTrack

// TODO: check that deleting a release will delete all media and tracks
@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = MediumRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("medium_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TrackRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "medium_id")
    val mediumId: Long,

    @ColumnInfo(name = "position")
    override val position: Int,
    @ColumnInfo(name = "number")
    override val number: String,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "length")
    override val length: Int?
) : Track

fun MusicBrainzTrack.toTrackRoomModel(mediumId: Long) =
    TrackRoomModel(
        id = id,
        mediumId = mediumId,
        position = position,
        number = number,
        title = title,
        length = length
    )
