package ly.david.data.persistence.label

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "releases_labels",
    primaryKeys = ["release_id", "label_id", "catalog_number"],
)
data class ReleaseLabel(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "label_id")
    val labelId: String,

    @ColumnInfo(name = "catalog_number")
    val catalogNumber: String = ""
)
