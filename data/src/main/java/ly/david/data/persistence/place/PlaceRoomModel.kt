package ly.david.data.persistence.place

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Coordinates
import ly.david.data.LifeSpan
import ly.david.data.Place
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "places")
data class PlaceRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "address")
    override val address: String,

    @ColumnInfo(name = "type")
    override val type: String?,

    @Embedded
    override val coordinates: Coordinates?,

    @Embedded
    override val lifeSpan: LifeSpan?,
) : RoomModel, Place

internal fun PlaceMusicBrainzModel.toPlaceRoomModel() =
    PlaceRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan
    )
