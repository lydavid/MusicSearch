package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

fun mapToInstrumentListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    description: String?,
    type: String?,
    visited: Boolean?,
    collected: Boolean?,
) = InstrumentListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    description = description,
    type = type,
    visited = visited == true,
    collected = collected == true,
)
