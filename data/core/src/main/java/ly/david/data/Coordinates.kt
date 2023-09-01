package ly.david.data

import kotlin.math.abs
import kotlinx.serialization.Serializable

/**
 * Used by both network and persistence models.
 * Because it's embedded, all of its fields should be nullable.
 */
interface Coordinates {
    val longitude: Double?
    val latitude: Double?
}

data class CoordinatesRoomModel(
    override val longitude: Double?,
    override val latitude: Double?,
) : Coordinates

fun CoordinatesRoomModel.toCoordinatesUiModel() = CoordinatesUiModel(
    longitude = longitude,
    latitude = latitude
)

fun CoordinatesMusicBrainzModel.toCoordinatesRoomModel() = CoordinatesRoomModel(
    longitude = longitude,
    latitude = latitude
)

data class CoordinatesUiModel(
    override val longitude: Double?,
    override val latitude: Double?,
) : Coordinates

@Serializable
data class CoordinatesMusicBrainzModel(
    override val longitude: Double? = null,
    override val latitude: Double? = null,
) : Coordinates

/**
 * Turns [Coordinates] to this format: 40.76688°N, 73.98905°W
 */
fun Coordinates.formatForDisplay(): String? {
    val lat = latitude
    val long = longitude
    if (lat == null || long == null) return null

    val latitudeString = if (lat < 0) "${abs(lat)}°S" else "$lat°N"
    val longitudeString = if (long < 0) "${abs(long)}°W" else "$long°E"

    return "$latitudeString, $longitudeString"
}
