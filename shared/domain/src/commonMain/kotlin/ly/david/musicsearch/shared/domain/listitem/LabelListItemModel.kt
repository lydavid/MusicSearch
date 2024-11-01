package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.label.Label
import ly.david.musicsearch.shared.domain.label.LabelWithCatalog

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    val catalogNumber: String? = null,
    override val visited: Boolean = false,
) : ListItemModel(), Label, Visitable

fun LabelWithCatalog.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        catalogNumber = catalogNumber,
    )
