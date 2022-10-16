package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

internal interface ILifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

/**
 * Used by both network and persistence models.
 */
internal data class LifeSpan(
    @ColumnInfo(name = "begin")
    @Json(name = "begin")
    override val begin: String? = null,

    @ColumnInfo(name = "end")
    @Json(name = "end")
    override val end: String? = null,

    /**
     * Despite SQL saying non-null, this could actually be null.
     */
    @ColumnInfo(name = "ended")
    @Json(name = "ended")
    override val ended: Boolean? = null
): ILifeSpan

internal fun ILifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = if (begin == end) {
        ""
    } else {
        end.transformThisIfNotNullOrEmpty { " to $it" }
    }
    return begin + end
}
