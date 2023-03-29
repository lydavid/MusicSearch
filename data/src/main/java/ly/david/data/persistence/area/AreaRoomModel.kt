package ly.david.data.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "area")
data class AreaRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "sort_name") override val sortName: String = "",
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @Embedded override val lifeSpan: LifeSpan? = null,
) : RoomModel, Area

fun AreaMusicBrainzModel.toAreaRoomModel() =
    AreaRoomModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        lifeSpan = lifeSpan,
    )
