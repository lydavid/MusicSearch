package ly.david.musicsearch.shared.domain.label

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

data class LabelDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
    val ipis: List<String>? = null,
    val isnis: List<String>? = null,
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val urls: List<RelationListItemModel> = listOf(),
) : Label
