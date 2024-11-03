package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

fun mapToLabelListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    labelCode: Int?,
    visited: Boolean?,
) = LabelListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = labelCode,
    visited = visited == true,
)

fun mapToLabelListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    labelCode: Int?,
    catalogNumber: String,
    visited: Boolean?,
) = LabelListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = labelCode,
    catalogNumber = catalogNumber,
    visited = visited == true,
)
