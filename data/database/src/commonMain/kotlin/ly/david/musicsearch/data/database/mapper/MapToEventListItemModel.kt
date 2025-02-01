package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel

fun mapToEventListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    time: String?,
    cancelled: Boolean?,
    begin: String?,
    end: String?,
    ended: Boolean?,
    visited: Boolean?,
) = EventListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    time = time,
    cancelled = cancelled,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    visited = visited == true,
)
