package ly.david.mbjc.data.persistence.label

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.network.LabelMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(tableName = "labels")
internal data class LabelRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @ColumnInfo(name = "label_code")
    override val labelCode: Int?,

    @ColumnInfo(name = "release_count", defaultValue = "null")
    val releaseCount: Int? = null,
) : RoomModel, Label

internal fun LabelMusicBrainzModel.toLabelRoomModel() =
    LabelRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )
