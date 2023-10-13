package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Area

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
