package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.CoverArtArchive
import ly.david.mbjc.data.network.ReleaseMusicBrainzModel
import ly.david.mbjc.data.network.getReleaseArtistCreditRoomModels
import ly.david.mbjc.data.persistence.release.ReleaseArtistCreditRoomModel
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel

internal data class ReleaseUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String,
    override val date: String? = null,
    override val status: String? = null,
    override val barcode: String? = null,
    override val statusId: String? = null,
    override val countryCode: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null,

    // TODO: ui doesn't need this
    override val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    val coverArtUrl: String? = null,

    val formats: String? = null,
    val tracks: String? = null,
    val artistCredits: List<ReleaseArtistCreditRoomModel> = listOf(),
    val releaseGroupId: String? = null
) : UiModel(), Release

internal fun ReleaseMusicBrainzModel.toReleaseUiModel() =
    ReleaseUiModel(
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
        asin = asin,
        quality = quality,
        coverArtUrl = null,
        artistCredits = getReleaseArtistCreditRoomModels(),
        releaseGroupId = releaseGroup?.id
    )

internal fun ReleaseRoomModel.toReleaseUiModel() =
    this.toReleaseUiModel(
        releaseArtistCreditRoomModel = listOf(),
        releaseGroupId = null
    )

internal fun ReleaseRoomModel.toReleaseUiModel(
    releaseArtistCreditRoomModel: List<ReleaseArtistCreditRoomModel>,
    releaseGroupId: String?
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
    releaseGroupId = releaseGroupId
)
