package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.series.Series

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: List<BasicAlias> = listOf(),
) : EntityListItemModel, Series, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: List<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
