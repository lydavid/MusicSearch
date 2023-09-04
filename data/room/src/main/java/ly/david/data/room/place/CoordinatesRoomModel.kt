package ly.david.data.room.place

import ly.david.data.core.Coordinates
import ly.david.data.musicbrainz.CoordinatesMusicBrainzModel

data class CoordinatesRoomModel(
    override val longitude: Double?,
    override val latitude: Double?,
) : Coordinates

fun CoordinatesMusicBrainzModel.toCoordinatesRoomModel() = CoordinatesRoomModel(
    longitude = longitude,
    latitude = latitude
)
