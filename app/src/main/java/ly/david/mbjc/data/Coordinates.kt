package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json

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
