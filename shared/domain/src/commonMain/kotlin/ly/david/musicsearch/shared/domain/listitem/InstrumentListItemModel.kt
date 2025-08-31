package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.instrument.Instrument

data class InstrumentListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val description: String = "",
    override val type: String = "",
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : EntityListItemModel, Instrument, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
