package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.label.Label

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    val catalogNumbers: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: List<BasicAlias> = listOf(),
) : EntityListItemModel, Label, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: List<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
