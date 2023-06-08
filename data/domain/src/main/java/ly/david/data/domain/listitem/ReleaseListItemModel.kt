package ly.david.data.domain.listitem

import ly.david.data.Release
import ly.david.data.getDisplayNames
import ly.david.data.getFormatsForDisplay
import ly.david.data.getTracksForDisplay
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.TextRepresentation
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.release.ReleaseForListItem

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
    val coverArtPath: String? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,
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
    coverArtPath = null,
    formattedArtistCredits = artistCredits.getDisplayNames()
)

fun ReleaseForListItem.toReleaseListItemModel() = ReleaseListItemModel(
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
    formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
    formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
    coverArtPath = release.coverArtPath,
    formattedArtistCredits = artistCreditNames,
    releaseCountries = releaseCountries,
)
