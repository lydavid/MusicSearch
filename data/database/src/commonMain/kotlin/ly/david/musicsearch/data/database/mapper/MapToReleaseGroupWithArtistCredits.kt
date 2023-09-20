package ly.david.musicsearch.data.database.mapper

import ly.david.data.core.ReleaseGroupForListItem

internal fun mapToReleaseGroupWithArtistCredits(
    id: String,
    name: String,
    firstReleaseDate: String,
    disambiguation: String,
    primaryType: String?,
    primaryTypeId: String?,
    secondaryTypes: List<String>?,
    secondaryTypeIds: List<String>?,
    formattedArtistCreditNames: String?,
) = ReleaseGroupForListItem(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    formattedArtistCreditNames = formattedArtistCreditNames.orEmpty(),
    thumbnailUrl = "", // TODO:
)
