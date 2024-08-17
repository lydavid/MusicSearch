package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.Area
import ly.david.musicsearch.shared.domain.area.ReleaseEvent

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
