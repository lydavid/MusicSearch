package ly.david.data.room.area

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel

data class AreaWithCountryCodes(
    @Embedded
    val area: AreaRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "area_id"
    )
    val countryCodes: List<CountryCode>,
) : RoomModel
