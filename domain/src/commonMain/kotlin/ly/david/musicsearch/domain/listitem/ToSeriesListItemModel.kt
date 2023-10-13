package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import lydavidmusicsearchdatadatabase.Series

fun Series.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )
