package ly.david.data.room.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Recording
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(tableName = "recording")
data class RecordingRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "title") override val name: String,
    @ColumnInfo(name = "first_release_date") override val firstReleaseDate: String? = null,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String = "",
    @ColumnInfo(name = "length") override val length: Int? = null,
    @ColumnInfo(name = "video") override val video: Boolean = false,
    @ColumnInfo(name = "isrcs") val isrcs: List<String>? = null,
) : RoomModel, Recording

fun RecordingMusicBrainzModel.toRoomModel() =
    RecordingRoomModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        length = length,
        video = video ?: false,
        isrcs = isrcs
    )
