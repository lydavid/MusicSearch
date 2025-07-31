package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel

data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val languages: List<String> = listOf(),
    override val iswcs: List<String> = listOf(),
    val attributes: List<WorkAttributeUiModel> = listOf(),
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: List<BasicAlias> = listOf(),
) : EntityListItemModel, Work, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: List<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
