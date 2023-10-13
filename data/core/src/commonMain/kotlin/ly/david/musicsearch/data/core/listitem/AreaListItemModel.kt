package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.area.Area
import ly.david.musicsearch.data.core.area.ReleaseEvent

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpanUiModel? = null,
    val countryCodes: List<String>? = null,
    val date: String? = null,
) : Area, ListItemModel()

fun ReleaseEvent.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    date = date,
    countryCodes = countryCode?.let { listOf(it) },
)
