package ly.david.data

import kotlin.math.abs
import kotlinx.serialization.Serializable

/**
 * Used by both network and persistence models.
 * Because it's embedded, all of its fields should be nullable.
 */
interface Coordinates {
    //    @Json(name = "longitude")
//    @ColumnInfo(name = "longitude")
    val longitude: Double?

    //    @Json(name = "latitude")
//    @ColumnInfo(name = "latitude")
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

//@Serializable
data class CoordinatesUiModel(
//    @Json(name = "longitude")
//    @ColumnInfo(name = "longitude")
    override val longitude: Double?,

//    @Json(name = "latitude")
//    @ColumnInfo(name = "latitude")
    override val latitude: Double?,
): Coordinates

@Serializable
data class CoordinatesMusicBrainzModel(
//    @Json(name = "longitude")
//    @ColumnInfo(name = "longitude")
    override val longitude: Double?,

//    @Json(name = "latitude")
//    @ColumnInfo(name = "latitude")
    override val latitude: Double?,
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
