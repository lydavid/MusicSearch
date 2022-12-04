package ly.david.data.persistence.area

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel

data class AreaWithIso(
    @Embedded
    val area: AreaRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "area_id"
    )
    val countryCodes: List<Iso3166_1>
) : RoomModel
