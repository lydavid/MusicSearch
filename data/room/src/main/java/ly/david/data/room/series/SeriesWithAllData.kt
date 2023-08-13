package ly.david.data.room.series

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.artist.UrlRelation

data class SeriesWithAllData(
    @Embedded
    val series: SeriesRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
) : RoomModel
