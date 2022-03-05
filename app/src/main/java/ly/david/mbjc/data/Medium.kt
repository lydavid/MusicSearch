package ly.david.mbjc.data

import com.squareup.moshi.Json

data class Medium(
    @Json(name = "position") val position: Int,
    @Json(name = "format") val format: String? = null,
    @Json(name = "format-id") val formatId: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "track-count") val trackCount: Int,
//    @Json(name = "track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @Json(name = "tracks") val tracks: List<Track>? = null,
)

fun List<Medium>.getFormatsForDisplay(): String {
    return ""
}

fun List<Medium>.getTracksForDisplay(): String {
    return ""
}
