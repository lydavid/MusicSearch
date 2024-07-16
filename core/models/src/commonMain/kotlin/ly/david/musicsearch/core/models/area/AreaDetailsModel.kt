package ly.david.musicsearch.core.models.area

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class AreaDetailsModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Area
