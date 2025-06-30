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
    visited: Boolean?,
) = mapToAreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    begin = begin,
    end = end,
    ended = ended,
    countryCode = countryCode,
    visited = visited,
    collected = false,
)

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
    visited: Boolean?,
    collected: Boolean?,
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
    visited = visited == true,
    collected = collected == true,
)
