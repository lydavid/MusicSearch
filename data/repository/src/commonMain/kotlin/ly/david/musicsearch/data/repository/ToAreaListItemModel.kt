package ly.david.musicsearch.data.repository

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Area

// TODO: remove: have db output single listitem
fun Area.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    sortName = sort_name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
)
