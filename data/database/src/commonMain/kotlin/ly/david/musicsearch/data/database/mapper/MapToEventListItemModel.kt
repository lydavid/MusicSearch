package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.EventListItemModel

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
)
