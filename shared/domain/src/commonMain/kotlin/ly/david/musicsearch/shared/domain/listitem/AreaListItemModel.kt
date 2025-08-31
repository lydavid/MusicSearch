package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.area.Area
import ly.david.musicsearch.shared.domain.area.ReleaseEvent

data class AreaListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String = "",
    override val type: String = "",
    override val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val countryCodes: ImmutableList<String> = persistentListOf(),
    val date: String? = "",
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : EntityListItemModel, Area, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}

fun ReleaseEvent.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    date = date,
    countryCodes = countryCode?.let { persistentListOf(it) } ?: persistentListOf(),
    visited = visited,
)
