package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.label.Label
import ly.david.musicsearch.core.models.label.LabelWithCatalog

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
