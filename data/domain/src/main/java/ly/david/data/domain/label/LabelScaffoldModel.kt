package ly.david.data.domain.label

import ly.david.data.core.Label
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.label.LabelWithAllData

data class LabelScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Label

internal fun LabelWithAllData.toLabelScaffoldModel() =
    LabelScaffoldModel(
        id = label.id,
        name = label.name,
        disambiguation = label.disambiguation,
        type = label.type,
        labelCode = label.labelCode,
        urls = urls.map { it.relation.toRelationListItemModel() },
    )
