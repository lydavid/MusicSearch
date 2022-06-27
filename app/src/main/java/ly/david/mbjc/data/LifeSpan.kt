package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

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

internal fun LifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = end.transformThisIfNotNullOrEmpty { " to $it" }
    return begin + end
}
