package ly.david.data.domain

import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.release.AreaWithReleaseDate
import ly.david.data.persistence.release.ReleaseArtistCreditRoomModel
import ly.david.data.persistence.release.ReleaseWithAllData
import ly.david.data.persistence.release.ReleaseWithReleaseCountries

// TODO: let's split this between ReleaseCardModel and ReleaseScaffoldModel
//  All current UiModel are more suited for CardModel
//  While ScaffoldModel includes additional information for display in TopBar and Details
data class ReleaseUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String,
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

    val formats: String? = null,
    val tracks: String? = null,

//    val artistCredits: List<ReleaseArtistCreditRoomModel> = listOf(),
//
//    val releaseGroup: ReleaseGroupUiModel? = null,
    val releaseEvents: List<ReleaseCountry> = listOf(),
//    val labels: List<LabelUiModel> = listOf()
) : UiModel(), Release

fun ReleaseMusicBrainzModel.toReleaseUiModel() = ReleaseUiModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    status = status,
    barcode = barcode,
    statusId = statusId,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    coverArtArchive = coverArtArchive,
    textRepresentation = textRepresentation,
    asin = asin,
    quality = quality,
    coverArtUrl = null,
)

fun ReleaseWithReleaseCountries.toReleaseUiModel(
//    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel> = listOf(),
//    releaseGroup: ReleaseGroupUiModel? = null
) = ReleaseUiModel(
    id = release.id,
    name = release.name,
    disambiguation = release.disambiguation,
    date = release.date,
    status = release.status,
    barcode = release.barcode,
    statusId = release.statusId,
    countryCode = release.countryCode,
    packaging = release.packaging,
    packagingId = release.packagingId,
    asin = release.asin,
    quality = release.quality,
    coverArtArchive = release.coverArtArchive,
    textRepresentation = release.textRepresentation,
    formats = release.formats,
    tracks = release.tracks,
    coverArtUrl = release.coverArtUrl,
    releaseEvents = releaseEvents,
//    artistCredits = releaseArtistCreditRoomModel,
//    releaseGroup = releaseGroup
)

data class ReleaseScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String,
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

    val formats: String? = null,
    val tracks: String? = null,

    // TODO: minor: consider mapping to a ui model
    //  which should make this stable (except lists aren't stable...)
    //  but we can at least make a model that only holds relevant info
    //  in this case, we don't need release_id and order (since we return them ordered)
    val artistCredits: List<ReleaseArtistCreditRoomModel> = listOf(),

    val releaseGroup: ReleaseGroupUiModel? = null,
    val areaWithReleaseDates: List<AreaWithReleaseDate> = listOf(),
    val labels: List<LabelUiModel> = listOf()
) : Release

fun ReleaseWithAllData.toScaffoldModel(
    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel> = listOf(),
    releaseGroup: ReleaseGroupUiModel? = null
) = ReleaseScaffoldModel(
    id = release.id,
    name = release.name,
    disambiguation = release.disambiguation,
    date = release.date,
    status = release.status,
    barcode = release.barcode,
    statusId = release.statusId,
    countryCode = release.countryCode,
    packaging = release.packaging,
    packagingId = release.packagingId,
    asin = release.asin,
    quality = release.quality,
    coverArtArchive = release.coverArtArchive,
    textRepresentation = release.textRepresentation,
    formats = release.formats,
    tracks = release.tracks,
    coverArtUrl = release.coverArtUrl,
    areaWithReleaseDates = areaWithReleaseDates,
    artistCredits = releaseArtistCreditRoomModel,
    releaseGroup = releaseGroup,
    labels = labels.map { it.toLabelUiModel() }
)
