package ly.david.musicsearch.shared.domain

import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.alias.BasicAlias

interface NameWithDisambiguationAndAliases : NameWithDisambiguation {
    val aliases: ImmutableList<BasicAlias>

    fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases
}
