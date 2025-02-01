package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.place.Coordinates
import ly.david.musicsearch.shared.domain.place.Place

data class PlaceListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val address: String,
    override val type: String? = null,
//    override val typeId: String?,
    override val coordinates: Coordinates? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    override val visited: Boolean = false,
) : ListItemModel(), Place, Visitable
