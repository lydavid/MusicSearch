package ly.david.data.room.place

import ly.david.data.core.Coordinates

data class CoordinatesRoomModel(
    override val longitude: Double?,
    override val latitude: Double?,
) : Coordinates
