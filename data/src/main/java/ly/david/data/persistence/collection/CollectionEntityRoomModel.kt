package ly.david.data.persistence.collection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "collection_entity",
    primaryKeys = ["id", "entity_id"],
    foreignKeys = [
        ForeignKey(
            entity = CollectionRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectionEntityRoomModel(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "entity_id") val entityId: String
)
