package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.WorkListItemModel
import lydavidmusicsearchdatadatabase.Work

fun Work.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = listOf()
    )
