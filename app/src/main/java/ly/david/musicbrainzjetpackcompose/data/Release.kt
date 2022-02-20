package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

// browse inc: artist-credits, labels, recordings, release-groups, media, discids, isrcs (with recordings)
// lookup inc: artist-credits, releases, isrcs, url-rels, labels, recordings, recording-level-rels, work-rels, work-level-rels, artist-rels
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

    //   "relations": [],

    // inc=media
    @Json(name = "media") val media: List<Medium>? = null,

    // inc=artist-credits
    @Json(name = "artist-credit") val artistCredits: List<ArtistCredit>? = null,

    // inc=label
    @Json(name = "label-info") val labelInfoList: List<LabelInfo>? = null,
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
