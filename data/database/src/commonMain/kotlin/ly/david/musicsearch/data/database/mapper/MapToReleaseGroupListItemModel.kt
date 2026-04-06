package ly.david.musicsearch.data.database.mapper

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSecondaryType

internal fun mapToReleaseGroupListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String,
    primaryTypeId: String,
    secondaryTypeIds: List<String>,
    formattedArtistCreditNames: String,
    imageId: Long?,
    source: ImageSource?,
    thumbnailUrl: String?,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = ReleaseGroupListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    primaryType = ReleaseGroupPrimaryType.fromId(primaryTypeId),
    secondaryTypes = secondaryTypeIds.mapNotNull { id ->
        ReleaseGroupSecondaryType.fromId(id)
    }.toPersistentList(),
    formattedArtistCredits = formattedArtistCreditNames,
    imageMetadata = mapToImageMetadata(
        id = imageId,
        thumbnailUrl = thumbnailUrl,
        source = source,
    ),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
