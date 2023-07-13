package ly.david.data.room.collection

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel

// TODO: don't need right now but a use case could be to mark whether current entity in already in this collection
//  wouldn't be accurate if the collection from MB and we haven't opened it yet (meaning entities will be empty)
//  but should be simple to check
data class CollectionWithEntities(
    @Embedded
    val collection: CollectionRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val entities: List<CollectionEntityRoomModel>,
) : RoomModel
