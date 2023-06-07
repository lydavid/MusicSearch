package ly.david.data.room.release.tracks

import androidx.room.ColumnInfo
import androidx.room.Embedded
import ly.david.data.room.RoomModel

data class TrackForListItem(
    @Embedded
    val track: TrackRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,
) : RoomModel
