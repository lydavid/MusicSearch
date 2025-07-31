package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel

internal fun mapToAreaListItemModel(
    id: String,
    name: String,
    sortName: String,
    disambiguation: String?,
    type: String?,
    begin: String?,
    end: String?,
    ended: Boolean?,
    countryCode: String?,
    visited: Boolean,
    collected: Boolean,
    aliasNames: String?,
    aliasLocales: String?,
) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    countryCodes = listOfNotNull(countryCode.takeIf { !it.isNullOrEmpty() }),
    visited = visited,
    collected = collected,
    aliases = combineToPrimaryAliases(aliasNames, aliasLocales),
)
