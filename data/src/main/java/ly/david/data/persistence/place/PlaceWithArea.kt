package ly.david.data.persistence.place

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ly.david.data.persistence.area.AreaPlace
import ly.david.data.persistence.area.AreaRoomModel

data class PlaceWithArea(
    @Embedded
    val place: PlaceRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AreaPlace::class,
            parentColumn = "place_id",
            entityColumn = "area_id"
        )
    )
    val area: AreaRoomModel?
)
