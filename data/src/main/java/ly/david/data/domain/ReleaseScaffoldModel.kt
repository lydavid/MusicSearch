package ly.david.data.domain

import ly.david.data.Release
import ly.david.data.getFormatsForDisplay
import ly.david.data.getTracksForDisplay
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.TextRepresentation
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

    override val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    override val textRepresentation: TextRepresentation? = null,
    val coverArtPath: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,

    val artistCredits: List<ArtistCreditUiModel> = listOf(),

    val releaseGroup: ReleaseGroupListItemModel? = null,
    val areas: List<AreaListItemModel> = listOf(),
    val labels: List<LabelListItemModel> = listOf(),

    val releaseLength: Int? = null,
    val hasNullLength: Boolean = false
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
    coverArtArchive = release.coverArtArchive,
    textRepresentation = release.textRepresentation,
    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    coverArtPath = release.coverArtPath,
    areas = areas.map { it.toAreaListItemModel() },
    artistCredits = artistCreditNamesWithResources.map {
        it.artistCreditNameRoomModel.toArtistCreditUiModel()
    },
    releaseGroup = releaseGroup?.toReleaseGroupListItemModel(),
    labels = labels.map { it.toLabelListItemModel() },
    releaseLength = releaseLength,
    hasNullLength = hasNullLength
)
