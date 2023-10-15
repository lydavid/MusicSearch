package ly.david.musicsearch.core.models.place

import kotlin.math.abs

data class CoordinatesUiModel(
    override val longitude: Double?,
    override val latitude: Double?,
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
