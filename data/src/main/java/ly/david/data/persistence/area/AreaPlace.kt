package ly.david.data.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.persistence.place.PlaceRoomModel

/**
 * Junction table for [AreaRoomModel] and [PlaceRoomModel].
 */
@Entity(
    tableName = "area_place",
    primaryKeys = ["area_id", "place_id"],
)
data class AreaPlace(
    @ColumnInfo(name = "area_id")
    val areaId: String,

    @ColumnInfo(name = "place_id")
    val placeId: String,
)
