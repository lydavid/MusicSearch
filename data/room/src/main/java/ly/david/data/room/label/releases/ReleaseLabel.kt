package ly.david.data.room.label.releases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ly.david.data.network.LabelInfo
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.release.ReleaseRoomModel

@Entity(
    tableName = "release_label",
    primaryKeys = ["release_id", "label_id", "catalog_number"],
    indices = [Index(value = ["label_id"])],
    foreignKeys = [
        ForeignKey(
            entity = LabelRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("label_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ReleaseRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReleaseLabel(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "label_id")
    val labelId: String,

    @ColumnInfo(name = "catalog_number")
    val catalogNumber: String = ""
)

fun List<LabelInfo>.toReleaseLabels(releaseId: String, labelId: String? = null): List<ReleaseLabel> {
    return this
        .filter { it.label?.id == labelId || labelId == null }
        .mapNotNull { labelInfo ->
            val label = labelInfo.label
            if (label == null) {
                null
            } else {
                ReleaseLabel(releaseId, label.id, labelInfo.catalogNumber.orEmpty())
            }
        }
}

