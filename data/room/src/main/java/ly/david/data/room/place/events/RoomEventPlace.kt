package ly.david.data.room.place.events

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.room.place.PlaceRoomModel

/**
 * Junction table for [EventRoomModel] and [PlaceRoomModel].
 */
@Entity(
    tableName = "event_place",
    primaryKeys = ["event_id", "place_id"],
)
data class RoomEventPlace(
    @ColumnInfo(name = "event_id")
    val eventId: String,

    @ColumnInfo(name = "place_id")
    val placeId: String,
)
