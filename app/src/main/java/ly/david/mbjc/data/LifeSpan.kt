package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json

/**
 * Used by both network and persistence models.
 */
internal data class LifeSpan(
    @ColumnInfo(name = "begin")
    @Json(name = "begin")
    val begin: String? = null,

    @ColumnInfo(name = "end")
    @Json(name = "end")
    val end: String? = null,

    @ColumnInfo(name = "ended")
    @Json(name = "ended")
    val ended: Boolean? = null
)
