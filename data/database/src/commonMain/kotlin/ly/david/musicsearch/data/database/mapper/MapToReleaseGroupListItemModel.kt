package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel

internal fun mapToReleaseGroupListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String,
    primaryType: String,
    secondaryTypes: List<String>,
    formattedArtistCreditNames: String,
    thumbnailUrl: String?,
    imageId: Long?,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = ReleaseGroupListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = imageId?.let { ImageId(it) },
    visited = visited == true,
    collected = collected == true,
    aliases = combineToPrimaryAliases(aliasNames, aliasLocales),
)
