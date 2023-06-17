package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "has_urls",
)
data class HasUrls(
    @PrimaryKey
    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    @ColumnInfo(name = "has_urls")
    val hasUrls: Boolean = false,
)
