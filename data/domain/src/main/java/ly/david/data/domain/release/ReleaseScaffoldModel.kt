package ly.david.data.domain.release

import ly.david.data.core.Release
import ly.david.data.core.getFormatsForDisplay
import ly.david.data.core.getTracksForDisplay
import ly.david.data.core.network.CoverArtArchiveUiModel
import ly.david.data.core.network.toCoverArtArchiveUiModel
import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModel
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.ReleaseGroupListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.domain.listitem.toLabelListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.domain.listitem.toReleaseGroupListItemModel
import ly.david.data.room.release.ReleaseWithAllData

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

    override val coverArtArchive: CoverArtArchiveUiModel = CoverArtArchiveUiModel(),
    override val textRepresentation: TextRepresentationUiModel? = null,
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
) : Release

internal fun ReleaseWithAllData.toReleaseScaffoldModel() = ReleaseScaffoldModel(
    id = release.id,
    name = release.name,
    disambiguation = release.disambiguation,
    date = release.date,
    barcode = release.barcode,
    status = release.status,
    statusId = release.statusId,
    countryCode = release.countryCode,
    packaging = release.packaging,
    packagingId = release.packagingId,
    asin = release.asin,
    quality = release.quality,
    coverArtArchive = release.coverArtArchive.toCoverArtArchiveUiModel(),
    textRepresentation = release.textRepresentation?.toTextRepresentationUiModel(),
    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    imageUrl = largeUrl,
    areas = areas.map { it.toAreaListItemModel() },
    artistCredits = artistCreditNamesWithEntities.map {
        it.artistCreditNameRoomModel.toArtistCreditUiModel()
    },
    releaseGroup = releaseGroup?.toReleaseGroupListItemModel(),
    labels = labels.map { it.toLabelListItemModel() },
    urls = urls.map { it.relation.toRelationListItemModel() },
    releaseLength = releaseLength,
    hasNullLength = hasNullLength
)
