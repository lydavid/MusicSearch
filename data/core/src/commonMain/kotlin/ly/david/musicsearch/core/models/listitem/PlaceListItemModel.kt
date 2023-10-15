package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.place.Coordinates
import ly.david.musicsearch.core.models.place.Place

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
