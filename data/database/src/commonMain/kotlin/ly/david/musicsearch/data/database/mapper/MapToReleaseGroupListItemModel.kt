package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.toPersistentList
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
    lastUpdated: Long?,
) = ReleaseGroupListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes.toPersistentList(),
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl,
    imageId = imageId?.let { ImageId(it) },
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
    lastUpdated = lastUpdated,
)
