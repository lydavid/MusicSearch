package ly.david.data.persistence.work

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Work
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "works")
data class WorkRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "language") override val language: String?,
    @ColumnInfo(name = "iswcs", defaultValue = "null") override val iswcs: List<String>? = null,
) : RoomModel, Work

internal fun WorkMusicBrainzModel.toWorkRoomModel() =
    WorkRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs
    )
