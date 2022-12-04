package ly.david.data.domain

import ly.david.data.Release
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries

data class ReleaseListItemModel(
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

    val formats: String? = null,
    val tracks: String? = null,
    val formattedArtistCredits: String? = null,

    val releaseCountries: List<ReleaseCountry> = listOf(),
) : ListItemModel(), Release

fun ReleaseMusicBrainzModel.toReleaseListItemModel() = ReleaseListItemModel(
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

fun ReleaseWithCreditsAndCountries.toReleaseListItemModel() = ReleaseListItemModel(
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
    formattedArtistCredits = artistCreditNames,
    releaseCountries = releaseCountries,
)
