package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

fun mapToLabelListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
    type: String?,
    labelCode: Int?,
) = LabelListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = labelCode,
)
