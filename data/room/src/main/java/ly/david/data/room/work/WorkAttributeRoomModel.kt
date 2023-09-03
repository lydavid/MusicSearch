package ly.david.data.room.work

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.data.core.WorkAttribute
import ly.david.data.musicbrainz.WorkAttributeMusicBrainzModel

@Entity(
    tableName = "work_attribute",
    foreignKeys = [
        ForeignKey(
            entity = WorkRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("work_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["work_id", "type"]
)
data class WorkAttributeRoomModel(
    @ColumnInfo(name = "work_id") val workId: String,
    @ColumnInfo(name = "type") override val type: String,
    @ColumnInfo(name = "type_id") override val typeId: String,
    @ColumnInfo(name = "value") override val value: String,
) : WorkAttribute

fun WorkAttributeMusicBrainzModel.toWorkAttributeRoomModel(workId: String) =
    WorkAttributeRoomModel(
        workId = workId,
        type = type,
        typeId = typeId,
        value = value
    )
