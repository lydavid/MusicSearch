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
    visited: Boolean?,
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
    visited = visited == true,
)
