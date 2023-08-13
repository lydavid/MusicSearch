package ly.david.data.room.instrument

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.artist.UrlRelation

data class InstrumentWithAllData(

    @Embedded
    val instrument: InstrumentRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
)
