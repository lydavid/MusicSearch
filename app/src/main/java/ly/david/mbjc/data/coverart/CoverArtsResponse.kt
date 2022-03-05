package ly.david.mbjc.data.coverart

import com.squareup.moshi.Json

data class CoverArtsResponse(
    @Json(name = "release") val release: String,
    @Json(name = "images") val coverArtUrls: List<CoverArtUrls>
)

data class CoverArtUrls(
    @Json(name = "edit") val edit: String,
    @Json(name = "id") val id: String,
    @Json(name = "image") val imageUrl: String? = null,
    @Json(name = "thumbnails") val thumbnailsUrls: ThumbnailsUrls? = null,
    @Json(name = "comment") val comment: String? = null,
    @Json(name = "approved") val approved: Boolean,
    @Json(name = "front") val front: Boolean,
    @Json(name = "back") val back: Boolean,
    @Json(name = "types") val types: List<String>? = null,
)

data class ThumbnailsUrls(
    @Json(name = "250") val resolution250Url: String? = null,
    @Json(name = "500") val resolution500Url: String? = null,
    @Json(name = "1200") val resolution1200Url: String? = null,
//    @Json(name = "small") val smallUrl: String? = null, //250
//    @Json(name = "large") val largeUrl: String? = null //500
)

// https://musicbrainz.org/doc/Cover_Art/Types
//enum class CoverArtType {
//    @Json(name = "Front") FRONT,
//    @Json(name = "Back") BACK,
//}
