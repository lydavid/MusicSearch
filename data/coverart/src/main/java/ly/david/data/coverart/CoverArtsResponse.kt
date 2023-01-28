package ly.david.data.coverart

import com.squareup.moshi.Json

/**
 * [Cover Art Archive API documentation](https://wiki.musicbrainz.org/Cover_Art_Archive/API#Cover_Art_Archive_Metadata)
 */
data class CoverArtsResponse(
    @Json(name = "images") val coverArtUrls: List<CoverArtUrls>,
    @Json(name = "release") val release: String // URL back to Music Brainz release
)

data class CoverArtUrls(
    @Json(name = "id") val id: String,

    /**
     * The full-size image. Its url is all we need, as we can build the thumbnail by appending -250.jpg
     */
    @Json(name = "image") val imageUrl: String? = null,

    @Json(name = "thumbnails") val thumbnailsUrls: ThumbnailsUrls? = null,
    @Json(name = "front") val front: Boolean,
//    @Json(name = "back") val back: Boolean,
    @Json(name = "types") val types: List<String>? = null,
)

/**
 * Note these URLs are missing the s in https, which is needed to load images into Coil.
 *
 * https://beta.musicbrainz.org/doc/Cover_Art_Archive/API
 * - "small" and "large" are deprecated keys and are equivalent to "250" and "500", respectively
 * - But some entries only uses these, so we should fall back to them
 */
data class ThumbnailsUrls(
    @Json(name = "250") val resolution250Url: String? = null,
    @Json(name = "500") val resolution500Url: String? = null,
    @Json(name = "1200") val resolution1200Url: String? = null,
    @Json(name = "small") val small: String? = null,
    @Json(name = "large") val larger: String? = null,
)

// There's more than 2 types: https://musicbrainz.org/doc/Cover_Art/Types
//enum class CoverArtType {
//    @Json(name = "Front") FRONT,
//    @Json(name = "Back") BACK,
//}

/**
 * Returns a cover art url.
 */
fun CoverArtsResponse.getFrontCoverArtUrl(): String? {
    // Note: MB doesn't fall back to any non-front covers
    return coverArtUrls.firstOrNull { it.front }?.imageUrl
}
