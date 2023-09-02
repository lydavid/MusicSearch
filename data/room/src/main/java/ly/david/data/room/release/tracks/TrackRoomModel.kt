package ly.david.data.room.release.tracks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.data.core.Track
import ly.david.data.network.TrackMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(
    tableName = "track",
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
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "medium_id", index = true) val mediumId: Long,
    @ColumnInfo(name = "position") override val position: Int,
    @ColumnInfo(name = "number") override val number: String,
    @ColumnInfo(name = "title") override val title: String,
    @ColumnInfo(name = "length") override val length: Int?,
    @ColumnInfo(name = "recording_id") val recordingId: String,
) : Track, RoomModel

fun TrackMusicBrainzModel.toTrackRoomModel(mediumId: Long) =
    TrackRoomModel(
        id = id,
        mediumId = mediumId,
        position = position,
        number = number,
        title = title,
        length = length,
        recordingId = recording.id
    )
