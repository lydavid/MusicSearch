package ly.david.data.room.work

import androidx.room.Embedded
import androidx.room.Relation

data class WorkWithAttributes(
    @Embedded
    val work: WorkRoomModel,

    @Relation(
        parentColumn = "id", // work.id
        entityColumn = "work_id",
    )
    val attributes: List<WorkAttributeRoomModel>,
)
