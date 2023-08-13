package ly.david.data.room.recording

import androidx.room.ColumnInfo
import androidx.room.Embedded
import ly.david.data.room.RoomModel

data class RecordingForListItem(
    @Embedded
    val recording: RecordingRoomModel,

    // This allows us to filter on this.
    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,
) : RoomModel
