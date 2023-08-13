package ly.david.data.room.label

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.artist.UrlRelation

data class LabelWithAllData(

    @Embedded
    val label: LabelRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
)
