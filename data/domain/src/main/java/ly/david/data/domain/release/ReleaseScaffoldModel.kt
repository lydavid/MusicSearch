package ly.david.data.domain.release

import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModel
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.ReleaseGroupListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Release

data class ReleaseScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String? = null,
    override val barcode: String? = null,
    override val status: String? = null,
    override val statusId: String? = null,
    override val countryCode: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null,

    val coverArtArchive: CoverArtArchiveUiModel = CoverArtArchiveUiModel(),
    val textRepresentation: TextRepresentationUiModel? = null,
    val imageUrl: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,

    val artistCredits: List<ArtistCreditUiModel> = listOf(),

    val releaseGroup: ReleaseGroupListItemModel? = null,
    val areas: List<AreaListItemModel> = listOf(),
    val labels: List<LabelListItemModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),

    val releaseLength: Int? = null,
    val hasNullLength: Boolean = false,
) : ly.david.data.core.Release

internal fun Release.toReleaseScaffoldModel(
    artistCreditNames: List<Artist_credit_name>,
    imageUrl: String?,
    urls: List<Relation>,
) = ReleaseScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    status = status,
    statusId = status_id,
    countryCode = country_code,
    packaging = packaging,
    packagingId = packaging_id,
    asin = asin,
    quality = quality,
//    coverArtArchive = release.coverArtArchive.toCoverArtArchiveUiModel(),
//    textRepresentation = release.textRepresentation?.toTextRepresentationUiModel(),
//    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
//    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    imageUrl = imageUrl,
//    areas = areas.map { it.toAreaListItemModel() },
    artistCredits = artistCreditNames.map { it.toArtistCreditUiModel() },
//    releaseGroup = releaseGroup?.toReleaseGroupListItemModel(),
//    labels = labels.map { it.toLabelListItemModel() },
    urls = urls.map { it.toRelationListItemModel() },
//    releaseLength = releaseLength,
//    hasNullLength = hasNullLength
)
