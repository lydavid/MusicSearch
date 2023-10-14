package ly.david.musicsearch.data.core.place

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.Place
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

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
