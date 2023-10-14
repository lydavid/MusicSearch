package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.AreaListItemModel

internal fun mapToAreaListItemModel(
    id: String,
    name: String,
    sortName: String,
    disambiguation: String?,
    type: String?,
    type_id: String?,
    begin: String?,
    end: String?,
    ended: Boolean?,
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
)
