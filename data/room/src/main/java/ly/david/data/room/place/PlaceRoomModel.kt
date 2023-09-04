package ly.david.data.room.place

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Place
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.room.common.LifeSpanRoomModel
import ly.david.data.room.RoomModel
import ly.david.data.room.common.toLifeSpanRoomModel

@Entity(tableName = "place")
data class PlaceRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "address") override val address: String,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @Embedded override val coordinates: CoordinatesRoomModel?,
    @Embedded override val lifeSpan: LifeSpanRoomModel?,
) : RoomModel, Place

fun PlaceMusicBrainzModel.toPlaceRoomModel() =
    PlaceRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        typeId = typeId,
        coordinates = coordinates?.toCoordinatesRoomModel(),
        lifeSpan = lifeSpan?.toLifeSpanRoomModel(),
    )
