package ly.david.data.domain.place

import ly.david.data.core.Coordinates
import ly.david.data.room.place.CoordinatesRoomModel

data class CoordinatesUiModel(
    override val longitude: Double?,
    override val latitude: Double?,
) : Coordinates

fun CoordinatesRoomModel.toCoordinatesUiModel() = CoordinatesUiModel(
    longitude = longitude,
    latitude = latitude
)
