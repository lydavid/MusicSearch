package ly.david.data.room.area.places

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import ly.david.data.room.place.PlaceRoomModel

/**
 * Junction table for [AreaRoomModel] and [PlaceRoomModel].
 */
@Entity(
    tableName = "area_place",
    primaryKeys = ["area_id", "place_id"],
    indices = [Index(value = ["place_id"])]
)
data class RoomAreaPlace(
    @ColumnInfo(name = "area_id")
    val areaId: String,

    @ColumnInfo(name = "place_id")
    val placeId: String,
)
