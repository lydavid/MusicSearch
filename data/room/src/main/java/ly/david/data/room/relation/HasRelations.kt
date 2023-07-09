package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "has_relations",
)
data class HasRelations(
    @PrimaryKey
    @ColumnInfo(name = "entity_id")
    val entityId: String,

    @ColumnInfo(name = "has_relations")
    val hasRelations: Boolean = false,
)
