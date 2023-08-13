package ly.david.data.room.work

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.artist.UrlRelation

data class WorkWithAllData(
    @Embedded
    val work: WorkRoomModel,

    @Relation(
        parentColumn = "id", // work.id
        entityColumn = "work_id",
    )
    val attributes: List<WorkAttributeRoomModel>,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
)
