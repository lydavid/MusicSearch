package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String,
    barcode: String,
    asin: String,
    quality: String,
    countryCode: String,
    statusId: String,
    packaging: String,
    packagingId: String,
    script: String,
    language: String,
    formattedArtistCreditNames: String?,
    thumbnailUrl: String?,
    imageId: Long?,
    releaseCountryCount: Long,
    visited: Boolean,
    collected: Boolean,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
    completeListenCount: Long,
    lastUpdated: Long?,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    asin = asin,
    quality = quality,
    countryCode = countryCode,
    status = ReleaseStatus.fromId(statusId),
    packaging = packaging,
    packagingId = packagingId,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = imageId?.let { ImageId(it) },
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited,
    collected = collected,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenState = toListenState(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    ),
    lastUpdated = lastUpdated,
)

/**
 * With [catalogNumber].
 */
internal fun mapToReleaseListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    date: String,
    barcode: String,
    asin: String,
    quality: String,
    countryCode: String,
    statusId: String,
    packaging: String,
    packagingId: String,
    script: String,
    language: String,
    formattedArtistCreditNames: String?,
    thumbnailUrl: String?,
    imageId: Long?,
    releaseCountryCount: Long,
    visited: Boolean,
    collected: Boolean,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
    completeListenCount: Long,
    lastUpdated: Long?,
    catalogNumber: String,
) = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    barcode = barcode,
    asin = asin,
    quality = quality,
    countryCode = countryCode,
    status = ReleaseStatus.fromId(statusId),
    packaging = packaging,
    packagingId = packagingId,
    textRepresentation = TextRepresentationUiModel(
        script = script,
        language = language,
    ),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = imageId?.let { ImageId(it) },
    releaseCountryCount = releaseCountryCount.toInt(),
    visited = visited,
    collected = collected,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenState = toListenState(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    ),
    lastUpdated = lastUpdated,
    catalogNumbers = catalogNumber,
)

private const val UNKNOWN_LISTENS_FLAG = -1L

private fun toListenState(
    listenCount: Long?,
    completeListenCount: Long,
) = when (listenCount) {
    null -> ReleaseListItemModel.ListenState.Hide
    UNKNOWN_LISTENS_FLAG -> ReleaseListItemModel.ListenState.Unknown
    else -> ReleaseListItemModel.ListenState.Known(
        listenCount = listenCount,
        completeListenCount = completeListenCount,
    )
}
