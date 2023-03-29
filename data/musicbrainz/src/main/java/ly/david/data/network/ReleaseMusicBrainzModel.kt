package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.NameWithDisambiguation
import ly.david.data.Release

data class ReleaseMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "date") override val date: String? = null,
    @Json(name = "status") override val status: String? = null,
    @Json(name = "status-id") override val statusId: String? = null,
    @Json(name = "barcode") override val barcode: String? = null,
    @Json(name = "country") override val countryCode: String? = null,
    @Json(name = "packaging") override val packaging: String? = null,
    @Json(name = "packaging-id") override val packagingId: String? = null,
    @Json(name = "asin") override val asin: String? = null,
    @Json(name = "quality") override val quality: String? = null,
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

data class ReleaseEvent(
    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "date") val date: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "name") override val name: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
) : NameWithDisambiguation
