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
): Coordinates

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
): Coordinates

@Serializable
data class CoordinatesMusicBrainzModel(
    override val longitude: Double? = null,
    override val latitude: Double? = null,
): Coordinates

/**
 * Turns [Coordinates] to this format: 40.76688°N, 73.98905°W
 */
fun CoordinatesUiModel.formatForDisplay(): String? {
    if (longitude == null || latitude == null) return null

    val lat = if (latitude < 0) "${abs(latitude)}°S" else "$latitude°N"
    val long = if (longitude < 0) "${abs(longitude)}°W" else "$longitude°E"

    return "$lat, $long"
}
