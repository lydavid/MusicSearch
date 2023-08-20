package ly.david.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import ly.david.data.common.transformThisIfNotNullOrEmpty

interface ILifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

/**
 * Used by both network and persistence models.
 */
data class LifeSpan(
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
    override val ended: Boolean? = null,
) : ILifeSpan

fun ILifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = if (begin == end) {
        ""
    } else {
        end.transformThisIfNotNullOrEmpty { " to $it" }
    }
    return begin + end
}
