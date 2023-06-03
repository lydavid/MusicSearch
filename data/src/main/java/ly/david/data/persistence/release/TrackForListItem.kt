package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import ly.david.data.persistence.RoomModel

data class TrackForListItem(
    @Embedded
    val track: TrackRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,
) : RoomModel
