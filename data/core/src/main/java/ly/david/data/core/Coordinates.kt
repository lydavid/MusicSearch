package ly.david.data.core

import kotlin.math.abs

interface Coordinates {
    val longitude: Double?
    val latitude: Double?
}

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
