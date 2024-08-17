package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

fun mapToInstrumentListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    description: String?,
    type: String?,
) = InstrumentListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    description = description,
    type = type,
)
