package ly.david.data.persistence.label

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Label
import ly.david.data.LifeSpan
import ly.david.data.network.LabelInfo
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "label")
data class LabelRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "label_code") override val labelCode: Int? = null,
    @Embedded val lifeSpan: LifeSpan? = null,
) : RoomModel, Label

fun LabelMusicBrainzModel.toLabelRoomModel() =
    LabelRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        labelCode = labelCode,
        lifeSpan = lifeSpan
    )

fun List<LabelInfo>.toRoomModels(): List<LabelRoomModel> {
    return this.mapNotNull { labelInfo ->
        val label = labelInfo.label
        if (label == null) {
            null
        } else {
            LabelRoomModel(
                id = label.id,
                name = label.name,
                disambiguation = label.disambiguation,
                type = label.type,
                typeId = label.typeId,
                labelCode = label.labelCode,
                lifeSpan = label.lifeSpan
            )
        }
    }
}
