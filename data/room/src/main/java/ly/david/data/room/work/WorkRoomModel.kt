package ly.david.data.room.work

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Work
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(tableName = "work")
data class WorkRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "language") override val language: String?,
    @ColumnInfo(name = "iswcs") override val iswcs: List<String>? = null,
) : RoomModel, Work

fun WorkMusicBrainzModel.toWorkRoomModel() =
    WorkRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        language = language,
        iswcs = iswcs
    )
