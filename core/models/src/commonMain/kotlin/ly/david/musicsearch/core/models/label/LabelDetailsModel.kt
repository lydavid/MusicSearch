package ly.david.musicsearch.core.models.label

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class LabelDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Label
