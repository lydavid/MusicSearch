package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel

internal fun mapToReleaseGroupListItemModel(
    id: String,
    name: String,
    firstReleaseDate: String,
    disambiguation: String,
    primaryType: String?,
    secondaryTypes: List<String>?,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
    visited: Boolean?,
) = ReleaseGroupListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes.orEmpty(),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    visited = visited == true,
)
