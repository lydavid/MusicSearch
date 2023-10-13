package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForListItem

internal fun mapToReleaseGroupForListItem(
    id: String,
    name: String,
    firstReleaseDate: String,
    disambiguation: String,
    primaryType: String?,
    secondaryTypes: List<String>?,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
) = ReleaseGroupForListItem(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    formattedArtistCreditNames = formattedArtistCreditNames,
    thumbnailUrl = thumbnailUrl,
)
