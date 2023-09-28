package ly.david.data.room.label

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Label
import ly.david.data.room.RoomModel
import ly.david.data.room.common.LifeSpanRoomModel

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
