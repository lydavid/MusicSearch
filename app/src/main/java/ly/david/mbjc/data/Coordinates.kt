package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import kotlin.math.abs

/**
 * Used by both network and persistence models.
 * Because it's embedded, all of its fields should be nullable.
 */
internal data class Coordinates(
    @Json(name = "longitude")
    @ColumnInfo(name = "longitude")
    val longitude: Double?,

    @Json(name = "latitude")
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
)

/**
 * Turns [Coordinates] to this format: 40.76688°N, 73.98905°W
 */
internal fun Coordinates.formatForDisplay(): String? {

    if (longitude == null || latitude == null) return null

    val lat = if (latitude < 0) "${abs(latitude)}°S" else "${latitude}°N"
    val long = if (longitude < 0) "${abs(longitude)}°W" else "${longitude}°E"

    return "$lat, $long"
}
