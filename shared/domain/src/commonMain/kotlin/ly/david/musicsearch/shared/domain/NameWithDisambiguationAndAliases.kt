package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.alias.BasicAlias

interface NameWithDisambiguationAndAliases : NameWithDisambiguation {
    val aliases: List<BasicAlias>

    fun withAliases(aliases: List<BasicAlias>): NameWithDisambiguationAndAliases
}
