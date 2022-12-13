package ly.david.data.persistence.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.persistence.place.PlaceRoomModel

/**
 * Junction table for [EventRoomModel] and [PlaceRoomModel].
 */
@Entity(
    tableName = "event_place",
    primaryKeys = ["event_id", "place_id"],
)
data class EventPlace(
    @ColumnInfo(name = "event_id")
    val eventId: String,

    @ColumnInfo(name = "place_id")
    val placeId: String,
)
