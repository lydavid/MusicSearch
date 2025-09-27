package ly.david.musicsearch.shared.domain.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias

data class FacetListItem(
    val id: String = "",
    override val name: String = "",
    override val disambiguation: String = "",
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
    val formattedArtistCredits: String = "",
    val count: Int = 0,
) : NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
