package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.Area
import ly.david.musicsearch.shared.domain.area.ReleaseEvent

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val lifeSpan: LifeSpanUiModel? = LifeSpanUiModel(),
    val countryCodes: List<String> = listOf(),
    val date: String? = "",
    override val visited: Boolean = false,
) : ListItemModel(), Area, Visitable

fun ReleaseEvent.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    date = date,
    countryCodes = listOfNotNull(countryCode),
    visited = visited,
)
