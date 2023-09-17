package ly.david.data.room.place

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.area.places.RoomAreaPlace
import ly.david.data.room.artist.UrlRelation

data class PlaceWithAllData(
    @Embedded
    val place: PlaceRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RoomAreaPlace::class,
            parentColumn = "place_id",
            entityColumn = "area_id"
        )
    )
    val area: AreaRoomModel?,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
)
