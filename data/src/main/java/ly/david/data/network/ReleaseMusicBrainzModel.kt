package ly.david.data.network

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import ly.david.data.NameWithDisambiguation
import ly.david.data.Release

// browse inc: artist-credits, labels, recordings, release-groups, media, discids, isrcs (with recordings)
// lookup inc: artist-credits, releases, isrcs, url-rels, labels, recordings, recording-level-rels, work-rels, work-level-rels, artist-rels
data class ReleaseMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "date") override val date: String? = null,
    @Json(name = "status") override val status: String? = null,
    @Json(name = "barcode") override val barcode: String? = null,
    @Json(name = "status-id") override val statusId: String? = null,

    // TODO: rather than using this field, use the first release-event
    //  or make a query to avoid storing redundant info
    @Json(name = "country") override val countryCode: String? = null,

    @Json(name = "packaging") override val packaging: String? = null,
    @Json(name = "packaging-id") override val packagingId: String? = null,
    @Json(name = "asin") override val asin: String? = null,
    @Json(name = "quality") override val quality: String? = null,

    // If there exists at least one `count`, then we should request CAA for its cover art.
    @Json(name = "cover-art-archive") override val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    @Json(name = "text-representation") override val textRepresentation: TextRepresentation? = null,

    @Json(name = "release-events") val releaseEvents: List<ReleaseEvent>? = null,

    // Use inc=media for subqueries. inc=recordings for release lookup
    @Json(name = "media") val media: List<MediumMusicBrainzModel>? = null,

    // Use inc=artist-credits for subqueries. inc=artists for release lookup
    @Json(name = "artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,

    // inc=labels
    @Json(name = "label-info") val labelInfoList: List<LabelInfo>? = null,

    // inc=release-groups
    @Json(name = "release-group") val releaseGroup: ReleaseGroupMusicBrainzModel? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Release

data class CoverArtArchive(
//    @Json(name = "darkened") val darkened: Boolean = false,
//    @Json(name = "artwork") val artwork: Boolean = false,
//    @Json(name = "back") val back: Boolean = false,
//    @Json(name = "front") val front: Boolean = false,

    @ColumnInfo(name = "cover_art_count", defaultValue = "0")
    @Json(name = "count")
    val count: Int = 0
)

data class TextRepresentation(
    /**
     * See: https://en.wikipedia.org/wiki/ISO_15924
     */
    @ColumnInfo(name = "script")
    @Json(name = "script")
    val script: String? = null,

    /**
     * See: https://en.wikipedia.org/wiki/ISO_639-3
     */
    @ColumnInfo(name = "language")
    @Json(name = "language")
    val language: String? = null,
)

data class ReleaseEvent(
    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "date") val date: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "name") override val name: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
) : NameWithDisambiguation
