package ly.david.musicsearch.core.models.place

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class PlaceScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
    override val coordinates: CoordinatesUiModel? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val area: AreaListItemModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Place
