package ly.david.musicsearch.data.coverart.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * See docs for [Cover Art Archive](https://musicbrainz.org/doc/Cover_Art_Archive/API)
 * and [Event Art Archive](https://musicbrainz.org/doc/Event_Art_Archive/API).
 *
 * Although, they differ with `release` and `event` fields, we don't use them, so we can use this one response.
 */
@Serializable
data class CoverArtsResponse(
    @SerialName("images") val coverArtUrls: List<CoverArtUrls>,
)

@Serializable
data class CoverArtUrls(
    /**
     * The full-size image. Not actually used at the moment.
     */
    @SerialName("image") val imageUrl: String? = null,

    @SerialName("thumbnails") val thumbnailsUrls: ThumbnailsUrls? = null,
    @SerialName("front") val front: Boolean,
    @SerialName("types") val types: List<String> = listOf(),
    @SerialName("comment") val comment: String = "",
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
