package ly.david.data.domain.label

import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Mb_relation

data class LabelScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Label

internal fun Label.toLabelScaffoldModel(
    urls: List<Mb_relation>,
) = LabelScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = label_code,
    urls = urls.map { it.toRelationListItemModel() },
)
