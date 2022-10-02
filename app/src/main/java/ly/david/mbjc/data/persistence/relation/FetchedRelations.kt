package ly.david.mbjc.data.persistence.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "has_relations",
)
internal data class HasRelationsRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    @ColumnInfo(name = "has_relations", defaultValue = "false")
    val hasRelations: Boolean = false,
)
