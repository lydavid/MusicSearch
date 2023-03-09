package ly.david.data.persistence.collection

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel

data class CollectionWithEntities(
    @Embedded
    val collection: CollectionRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val entities: List<CollectionEntityRoomModel>
) : RoomModel
