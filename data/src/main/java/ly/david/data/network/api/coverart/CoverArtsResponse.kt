package ly.david.data.network.api.coverart

import com.squareup.moshi.Json

data class CoverArtsResponse(

    @Json(name = "images") val coverArtUrls: List<CoverArtUrls>,

    // URL back to Music Brainz release
    @Json(name = "release") val release: String
)

data class CoverArtUrls(
//    @Json(name = "edit") val edit: String,
    @Json(name = "id") val id: String,

    // Seems to be the full-size image
    @Json(name = "image") val imageUrl: String? = null,


    @Json(name = "thumbnails") val thumbnailsUrls: ThumbnailsUrls? = null,
//    @Json(name = "comment") val comment: String? = null,
//    @Json(name = "approved") val approved: Boolean,
    @Json(name = "front") val front: Boolean,
//    @Json(name = "back") val back: Boolean,
    @Json(name = "types") val types: List<String>? = null,
)

/**
 * Note these URLs are missing the s in https, which is needed to load images into Coil.
 *
 * https://beta.musicbrainz.org/doc/Cover_Art_Archive/API
 * > "small" and "large" are deprecated keys and are equivalent to "250" and "500", respectively
 */
data class ThumbnailsUrls(
    @Json(name = "250") val resolution250Url: String? = null,
    @Json(name = "500") val resolution500Url: String? = null,
    @Json(name = "1200") val resolution1200Url: String? = null,
)

// There's more than 2 types: https://musicbrainz.org/doc/Cover_Art/Types
//enum class CoverArtType {
//    @Json(name = "Front") FRONT,
//    @Json(name = "Back") BACK,
//}

/**
 * Returns an appropriate small [ThumbnailsUrls.resolution250Url].
 */
fun CoverArtsResponse.getSmallCoverArtUrl(): String? {
    val frontSmallUrl = coverArtUrls.first { it.front }.thumbnailsUrls?.resolution250Url
    return frontSmallUrl ?: coverArtUrls.first().thumbnailsUrls?.resolution250Url
}
