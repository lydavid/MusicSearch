package ly.david.data.domain

import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.network.getReleaseArtistCreditRoomModels
import ly.david.data.network.toLabelRoomModels
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.release.ReleaseArtistCreditRoomModel
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.ReleaseWithAllData
import ly.david.data.persistence.release.ReleaseWithReleaseCountries

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

    // TODO: minor: consider mapping to a ui model
    //  which should make this stable (except lists aren't stable...)
    //  but we can at least make a model that only holds relevant info
    //  in this case, we don't need release_id and order (since we return them ordered)
    val artistCredits: List<ReleaseArtistCreditRoomModel> = listOf(),

    val releaseGroup: ReleaseGroupUiModel? = null,
    val releaseEvents: List<ReleaseCountry> = listOf(),
    val labels: List<LabelUiModel> = listOf()
) : UiModel(), Release

fun ReleaseMusicBrainzModel.toReleaseUiModel() =
    toReleaseUiModel(releaseGroup = null)

// TODO: with paging, we never convert mb model to ui model, only room model to ui model
//  that means we don't have to do 2 mappings
//  can we do something when doing a lookup?
fun ReleaseMusicBrainzModel.toReleaseUiModel(
    releaseGroup: ReleaseGroupUiModel? = null
) = ReleaseUiModel(
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
        artistCredits = getReleaseArtistCreditRoomModels(),

        // TODO: missing format/tracks

        releaseGroup = releaseGroup,
        labels = labelInfoList?.toLabelRoomModels()?.map { it.toLabelUiModel() }.orEmpty()
    )

fun ReleaseRoomModel.toReleaseUiModel(
    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel>,
    releaseGroup: ReleaseGroupUiModel?
) = ReleaseUiModel(
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
    asin = asin,
    quality = quality,
    coverArtArchive = coverArtArchive,
    formats = formats,
    tracks = tracks,
    coverArtUrl = coverArtUrl,
    artistCredits = releaseArtistCreditRoomModel,
    releaseGroup = releaseGroup
)

fun ReleaseWithReleaseCountries.toReleaseUiModel(
    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel> = listOf(),
    releaseGroup: ReleaseGroupUiModel? = null
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
    artistCredits = releaseArtistCreditRoomModel,
    releaseGroup = releaseGroup
)

fun ReleaseWithAllData.toReleaseUiModel(
    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel> = listOf(),
    releaseGroup: ReleaseGroupUiModel? = null
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
    artistCredits = releaseArtistCreditRoomModel,
    releaseGroup = releaseGroup,
    labels = labels.map { it.toLabelUiModel() }
)
