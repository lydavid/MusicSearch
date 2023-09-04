package ly.david.data.room.label

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Label
import ly.david.data.room.LifeSpanRoomModel
import ly.david.data.musicbrainz.LabelInfo
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.room.RoomModel
import ly.david.data.room.toLifeSpanRoomModel

@Entity(tableName = "label")
data class LabelRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "label_code") override val labelCode: Int? = null,
    @Embedded val lifeSpan: LifeSpanRoomModel? = null,
) : RoomModel, Label

fun LabelMusicBrainzModel.toLabelRoomModel() =
    LabelRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        labelCode = labelCode,
        lifeSpan = lifeSpan?.toLifeSpanRoomModel(),
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
                lifeSpan = label.lifeSpan?.toLifeSpanRoomModel(),
            )
        }
    }
}
