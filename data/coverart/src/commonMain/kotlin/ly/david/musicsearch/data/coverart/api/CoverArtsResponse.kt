package ly.david.musicsearch.data.coverart.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [Cover Art Archive API documentation](https://wiki.musicbrainz.org/Cover_Art_Archive/API#Cover_Art_Archive_Metadata)
 */
@Serializable
data class CoverArtsResponse(
    @SerialName("images") val coverArtUrls: List<CoverArtUrls>,
    @SerialName("release") val releaseUrl: String,
)

@Serializable
data class CoverArtUrls(
    @SerialName("id") val id: String,

    /**
     * The full-size image. Its url is all we need, as we can build the thumbnail by appending -250.jpg
     */
    @SerialName("image") val imageUrl: String? = null,

    @SerialName("thumbnails") val thumbnailsUrls: ThumbnailsUrls? = null,
    @SerialName("front") val front: Boolean,
//    @SerialName("back") val back: Boolean,
    @SerialName("types") val types: List<String>? = null,
)

/**
 * Note these URLs are missing the s in https, which is needed to load images into Coil.
 *
 * https://beta.musicbrainz.org/doc/Cover_Art_Archive/API
 * - "small" and "large" are deprecated keys and are equivalent to "250" and "500", respectively
 * - But some entries only uses these, so we should fall back to them
 */
@Serializable
data class ThumbnailsUrls(
    @SerialName("250") val resolution250Url: String? = null,
    @SerialName("500") val resolution500Url: String? = null,
    @SerialName("1200") val resolution1200Url: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("large") val large: String? = null,
)

fun CoverArtsResponse.getFrontThumbnailCoverArtUrl(): String? {
    // Note: MB doesn't fall back to any non-front covers
    return coverArtUrls.firstOrNull { it.front }?.thumbnailsUrls?.resolution250Url
        ?: coverArtUrls.firstOrNull { it.front }?.thumbnailsUrls?.small
        ?: coverArtUrls.firstOrNull { it.front }?.thumbnailsUrls?.resolution500Url
        ?: coverArtUrls.firstOrNull { it.front }?.thumbnailsUrls?.large
}

fun CoverArtsResponse.getFrontLargeCoverArtUrl(): String? {
    return coverArtUrls.firstOrNull { it.front }?.imageUrl
}
