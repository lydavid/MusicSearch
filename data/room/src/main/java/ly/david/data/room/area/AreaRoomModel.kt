package ly.david.data.room.area

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Area
import ly.david.data.core.LifeSpanRoomModel
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.room.RoomModel
import ly.david.data.core.toLifeSpanRoomModel

@Entity(tableName = "area")
data class AreaRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "sort_name") override val sortName: String = "",
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @Embedded override val lifeSpan: LifeSpanRoomModel? = null,
) : RoomModel, Area

fun AreaMusicBrainzModel.toAreaRoomModel() =
    AreaRoomModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        lifeSpan = lifeSpan?.toLifeSpanRoomModel(),
    )
