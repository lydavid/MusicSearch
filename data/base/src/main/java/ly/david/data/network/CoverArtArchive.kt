package ly.david.data.network

import androidx.room.ColumnInfo
import com.squareup.moshi.Json

data class CoverArtArchive(
//    @Json(name = "darkened") val darkened: Boolean = false,
//    @Json(name = "artwork") val artwork: Boolean = false,
//    @Json(name = "back") val back: Boolean = false,
//    @Json(name = "front") val front: Boolean = false,

    @ColumnInfo(name = "cover_art_count")
    @Json(name = "count")
    val count: Int = 0
)
