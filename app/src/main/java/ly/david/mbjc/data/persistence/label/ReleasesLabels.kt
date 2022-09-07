package ly.david.mbjc.data.persistence.label

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "releases_labels",
    primaryKeys = ["release_id", "label_id"]
)
internal data class ReleasesLabels(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "label_id")
    val labelId: String,
)
