package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel

fun mapToSeriesListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
) = SeriesListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
)
