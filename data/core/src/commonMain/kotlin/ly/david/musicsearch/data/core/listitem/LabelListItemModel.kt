package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Label
import ly.david.musicsearch.data.core.label.LabelWithCatalog

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    val catalogNumber: String? = null,
) : Label, ListItemModel()

fun LabelWithCatalog.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        catalogNumber = catalogNumber,
    )
