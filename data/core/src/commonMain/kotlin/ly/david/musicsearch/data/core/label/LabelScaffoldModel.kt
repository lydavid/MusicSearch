package ly.david.musicsearch.data.core.label

import ly.david.musicsearch.data.core.Label
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class LabelScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Label
