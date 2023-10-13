package ly.david.musicsearch.domain.release

import ly.david.musicsearch.data.core.area.ReleaseEvent
import ly.david.musicsearch.data.core.getFormatsForDisplay
import ly.david.musicsearch.data.core.getTracksForDisplay
import ly.david.musicsearch.data.core.label.LabelWithCatalog
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.data.core.listitem.LabelListItemModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.listitem.toAreaListItemModel
import ly.david.musicsearch.data.core.listitem.toLabelListItemModel
import ly.david.musicsearch.data.core.release.CoverArtArchiveUiModel
import ly.david.musicsearch.data.core.release.FormatTrackCount
import ly.david.musicsearch.data.core.release.Release
import ly.david.musicsearch.data.core.release.ReleaseForDetails
import ly.david.musicsearch.data.core.release.TextRepresentationUiModel
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.domain.artist.toArtistCreditUiModel
import lydavidmusicsearchdatadatabase.Artist_credit_name

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

    val releaseGroup: ReleaseGroupForRelease? = null,
    val areas: List<AreaListItemModel> = listOf(),
    val labels: List<LabelListItemModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),

    val releaseLength: Int? = null,
    val hasNullLength: Boolean = false,
) : Release

internal fun ReleaseForDetails.toReleaseScaffoldModel(
    artistCreditNames: List<Artist_credit_name>,
    releaseGroup: ReleaseGroupForRelease,
    formatTrackCounts: List<FormatTrackCount>,
    labels: List<LabelWithCatalog>,
    releaseEvents: List<ReleaseEvent>,
    urls: List<RelationListItemModel>,
) = ReleaseScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    status = status,
    statusId = statusId,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    asin = asin,
    quality = quality,
    coverArtArchive = CoverArtArchiveUiModel(
        count = coverArtCount,
    ),
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    imageUrl = imageUrl,
    areas = releaseEvents.map { it.toAreaListItemModel() },
    artistCredits = artistCreditNames.map { it.toArtistCreditUiModel() },
    releaseGroup = releaseGroup,
    labels = labels.map { it.toLabelListItemModel() },
    urls = urls,
    releaseLength = releaseLength,
    hasNullLength = hasNullLength,
)
