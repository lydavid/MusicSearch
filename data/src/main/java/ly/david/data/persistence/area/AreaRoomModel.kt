package ly.david.data.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "areas")
data class AreaRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @Embedded
    override val lifeSpan: LifeSpan?,

    @ColumnInfo(name = "release_count", defaultValue = "null")
    val releaseCount: Int? = null,
) : RoomModel, Area

fun AreaMusicBrainzModel.toAreaRoomModel() =
    AreaRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
