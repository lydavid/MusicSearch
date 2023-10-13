package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.musicsearch.data.core.release.CoverArtArchiveUiModel
import ly.david.musicsearch.data.core.release.TextRepresentationUiModel

// Although SQLDelight generates models for us, their types are based on the function names.
internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String?,
    barcode: String?,
    asin: String?,
    quality: String?,
    countryCode: String?,
    status: String?,
    statusId: String?,
    packaging: String?,
    packagingId: String?,
    script: String?,
    language: String?,
    coverArtCount: Int,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
    releaseCountryCount: Long,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    asin = asin,
    quality = quality,
    countryCode = countryCode,
    status = status,
    statusId = statusId,
    packaging = packaging,
    packagingId = packagingId,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    coverArtArchive = CoverArtArchiveUiModel(
        count = coverArtCount,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    releaseCountryCount = releaseCountryCount.toInt(),
)
