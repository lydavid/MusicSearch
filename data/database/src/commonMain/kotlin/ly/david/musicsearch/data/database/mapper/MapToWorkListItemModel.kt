package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

fun mapToWorkListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    language: String?,
    iswcs: List<String>?,
    visited: Boolean?,
    collected: Boolean?,
) = WorkListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    language = language,
    iswcs = iswcs,
    visited = visited == true,
    collected = collected == true,
)
