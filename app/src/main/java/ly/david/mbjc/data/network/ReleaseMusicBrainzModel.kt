package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.LabelInfo
import ly.david.mbjc.data.NameWithDisambiguation
import ly.david.mbjc.data.Release

// browse inc: artist-credits, labels, recordings, release-groups, media, discids, isrcs (with recordings)
// lookup inc: artist-credits, releases, isrcs, url-rels, labels, recordings, recording-level-rels, work-rels, work-level-rels, artist-rels
internal data class ReleaseMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "date") override val date: String? = null,
    @Json(name = "status") override val status: String? = null,
    @Json(name = "barcode") override val barcode: String? = null,
    @Json(name = "status-id") override val statusId: String? = null,
    @Json(name = "country") override val countryCode: String? = null,
    @Json(name = "packaging") override val packaging: String? = null,
    @Json(name = "packaging-id") override val packagingId: String? = null,
    @Json(name = "asin") override val asin: String? = null,
    @Json(name = "quality") override val quality: String? = null,

    // TODO: these can be embedded for room model
    @Json(name = "cover-art-archive") val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    @Json(name = "text-representation") val textRepresentation: TextRepresentation? = null,

    @Json(name = "release-events") val releaseEvents: List<ReleaseEvent>? = null,

    //   "relations": [],

    // inc=media
    @Json(name = "media") val media: List<MediumMusicBrainzModel>? = null,

    // inc=artist-credits
    @Json(name = "artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,

    // inc=label
    @Json(name = "label-info") val labelInfoList: List<LabelInfo>? = null,
) : MusicBrainzModel(), Release

internal data class CoverArtArchive(
    @Json(name = "darkened") val darkened: Boolean = false,
    @Json(name = "artwork") val artwork: Boolean = false,
    @Json(name = "back") val back: Boolean = false,
    @Json(name = "front") val front: Boolean = false,
    @Json(name = "count") val count: Int = 0
)

internal data class TextRepresentation(
    @Json(name = "script") val script: String? = null,
    @Json(name = "language") val language: String? = null,
)

internal data class ReleaseEvent(
    @Json(name = "date") val date: String? = null,
    @Json(name = "area") val area: Area? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "name") override val name: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
) : NameWithDisambiguation
