package ly.david.data.room.area

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.artist.UrlRelation

data class AreaWithAllData(
    @Embedded
    val area: AreaRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "area_id"
    )
    val countryCodes: List<CountryCode>,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
) : RoomModel
