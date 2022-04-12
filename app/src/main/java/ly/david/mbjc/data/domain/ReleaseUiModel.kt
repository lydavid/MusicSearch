package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.MusicBrainzRelease
import ly.david.mbjc.data.persistence.ReleaseRoomModel

data class ReleaseUiModel(
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

    val formats: String? = null,
    val tracks: String? = null

) : UiModel(), Release

fun MusicBrainzRelease.toReleaseUiModel() =
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
        asin = asin,
        quality = quality
    )

fun ReleaseRoomModel.toReleaseUiModel() =
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
        asin = asin,
        quality = quality,
        formats = formats,
        tracks = tracks
    )
