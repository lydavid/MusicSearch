package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Coordinates
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.Place

data class PlaceListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpanUiModel? = null,

    val area: AreaListItemModel? = null,
) : Place, ListItemModel()
