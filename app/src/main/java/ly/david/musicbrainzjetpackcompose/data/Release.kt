package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

class Release(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "disambiguation") val disambiguation: String = "",
    @Json(name = "date") val date: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "barcode") val barcode: String? = null,
    @Json(name = "status-id") val statusId: String? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "packaging") val packaging: String? = null,
    @Json(name = "packaging-id") val packagingId: String? = null,
    @Json(name = "asin") val asin: String? = null,
    @Json(name = "quality") val quality: String? = null,
    @Json(name = "cover-art-archive") val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    @Json(name = "text-representation") val textRepresentation: TextRepresentation? = null,
    @Json(name = "release-events") val releaseEvents: List<ReleaseEvent>? = null,

    // lookup inc=media
    @Json(name = "media") val media: List<Media>? = null
)

data class CoverArtArchive(
    @Json(name = "darkened") val darkened: Boolean = false,
    @Json(name = "artwork") val artwork: Boolean = false,
    @Json(name = "back") val back: Boolean = false,
    @Json(name = "front") val front: Boolean = false,
    @Json(name = "count") val count: Int = 0
)

data class TextRepresentation(
    @Json(name = "script") val script: String? = null,
    @Json(name = "language") val language: String? = null,
)

data class ReleaseEvent(
    @Json(name = "date") val date: String? = null,
    @Json(name = "area") val area: Area? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "disambiguation") val disambiguation: String? = null,
)

data class Area(
    @Json(name = "id") val id: String,
    @Json(name = "iso-3166-1-codes") val isoCodes: List<String>,
)

data class Media(
    @Json(name = "title") val title: String? = null,
    @Json(name = "track-count") val trackCount: Int,
    @Json(name = "position") val position: Int,
    @Json(name = "format") val format: String? = null,
    @Json(name = "format-id") val formatId: String? = null
)
