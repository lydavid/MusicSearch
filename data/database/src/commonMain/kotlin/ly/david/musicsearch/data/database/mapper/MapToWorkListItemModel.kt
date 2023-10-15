package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.core.models.listitem.WorkListItemModel

fun mapToWorkListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    language: String?,
    iswcs: List<String>?,
) = WorkListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    language = language,
    iswcs = iswcs,
)
