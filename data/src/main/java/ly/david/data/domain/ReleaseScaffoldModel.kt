package ly.david.data.domain

import ly.david.data.AreaType
import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.network.getFormatsForDisplay
import ly.david.data.network.getTracksForDisplay
import ly.david.data.network.toLabelListItemModels
import ly.david.data.persistence.release.ReleaseWithAllData

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
    val coverArtUrl: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,

    val artistCredits: List<ArtistCreditUiModel> = listOf(),

    val releaseGroup: ReleaseGroupListItemModel? = null,
    val areas: List<AreaListItemModel> = listOf(),
    val labels: List<LabelListItemModel> = listOf()
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
    formattedFormats = release.formats,
    formattedTracks = release.tracks,
    coverArtUrl = release.coverArtUrl,
    areas = areas.map { it.toAreaListItemModel() },
    artistCredits = artistCreditNamesWithResources.map {
        it.artistCreditNameRoomModel.toArtistCreditUiModel()
    },
    releaseGroup = releaseGroup?.toReleaseGroupListItemModel(),
    labels = labels.map { it.toLabelListItemModel() }
)

internal fun ReleaseMusicBrainzModel.toReleaseScaffoldModel() = ReleaseScaffoldModel(
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
    coverArtArchive = coverArtArchive,
    textRepresentation = textRepresentation,
    formattedFormats = media.getFormatsForDisplay(),
    formattedTracks = media.getTracksForDisplay(),
    coverArtUrl = null,
    areas = releaseEvents?.mapNotNull {
        it.area?.toAreaListItemModel(it.date)?.copy(type = AreaType.COUNTRY)
    }.orEmpty(),
    artistCredits = artistCredits.toArtistCreditUiModels(),
    releaseGroup = releaseGroup?.toReleaseGroupListItemModel(),
    labels = labelInfoList?.toLabelListItemModels().orEmpty()
)
