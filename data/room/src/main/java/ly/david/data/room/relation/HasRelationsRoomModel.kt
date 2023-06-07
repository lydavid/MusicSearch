package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "has_relations",
)
data class HasRelationsRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    @ColumnInfo(name = "has_relations")
    val hasRelations: Boolean = false,
)
