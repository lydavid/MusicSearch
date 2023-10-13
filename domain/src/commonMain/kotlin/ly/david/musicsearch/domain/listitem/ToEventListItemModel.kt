package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.EventListItemModel
import lydavidmusicsearchdatadatabase.Event

fun Event.toEventListItemModel() =
    EventListItemModel(
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
