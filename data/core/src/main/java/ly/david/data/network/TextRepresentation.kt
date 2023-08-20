package ly.david.data.network

import androidx.room.ColumnInfo
import com.squareup.moshi.Json

data class TextRepresentation(
    /**
     * See: https://en.wikipedia.org/wiki/ISO_15924
     */
    @ColumnInfo(name = "script")
    @Json(name = "script")
    val script: String? = null,

    /**
     * See: https://en.wikipedia.org/wiki/ISO_639-3
     */
    @ColumnInfo(name = "language")
    @Json(name = "language")
    val language: String? = null,
)
