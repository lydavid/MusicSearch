package ly.david.musicsearch.data.database.mapper

import ly.david.data.core.release.ReleaseForListItem

// Although SQLDelight generates models for us, their types are based on the function names.
internal fun mapToReleaseForListItem(
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
) = ReleaseForListItem(
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
    script = script,
    language = language,
    coverArtCount = coverArtCount,
    formattedArtistCreditNames = formattedArtistCreditNames,
    thumbnailUrl = thumbnailUrl,
    releaseCountryCount = releaseCountryCount.toInt(),
)
