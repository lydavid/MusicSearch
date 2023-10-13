package ly.david.musicsearch.domain.label

import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Label

data class LabelScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.Label

internal fun Label.toLabelScaffoldModel(
    urls: List<RelationListItemModel>,
) = LabelScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    labelCode = label_code,
    urls = urls,
)
