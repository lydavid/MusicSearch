package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.data.Track
import ly.david.data.network.TrackMusicBrainzModel
import ly.david.data.persistence.RoomModel

// TODO: medium_id column references a foreign key but it is not part of an index. This may trigger full table scans whenever parent table is modified so you are highly advised to create an index that covers this column. - ly.david.mbjc.data.persistence.release.TrackRoomModel
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

    @ColumnInfo(name = "medium_id", index = true)
    val mediumId: Long,

    @ColumnInfo(name = "position")
    override val position: Int,
    @ColumnInfo(name = "number")
    override val number: String,
    @ColumnInfo(name = "title")
    override val title: String,
    @ColumnInfo(name = "length")
    override val length: Int?,

    // TODO: when adding a new required field, need to specify defaultValue for migration
    //  but now we would have to empty check this everywhere.
    //  since we haven't released yet, let's just destructive migrate and get rid of this.
    @ColumnInfo(name = "recording_id", defaultValue = "")
    val recordingId: String,
) : Track, RoomModel

internal fun TrackMusicBrainzModel.toTrackRoomModel(mediumId: Long) =
    TrackRoomModel(
        id = id,
        mediumId = mediumId,
        position = position,
        number = number,
        title = title,
        length = length,
        recordingId = recording.id
    )
