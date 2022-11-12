package ly.david.data.persistence.label

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Label
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "labels")
data class LabelRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String? = null,

    @ColumnInfo(name = "type")
    override val type: String? = null,

    @ColumnInfo(name = "label_code")
    override val labelCode: Int? = null,
) : RoomModel, Label

internal fun LabelMusicBrainzModel.toLabelRoomModel() =
    LabelRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )
