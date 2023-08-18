package ly.david.data.room.event

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.artist.UrlRelation

data class EventWithAllData(

    @Embedded
    val event: EventRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
)
