package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel

data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val type: String = "",
    override val languages: ImmutableList<String> = persistentListOf(),
    override val iswcs: ImmutableList<String> = persistentListOf(),
    val attributes: ImmutableList<WorkAttributeUiModel> = persistentListOf(),
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
    val listenState: ListenState = ListenState.Hide,
) : EntityListItemModel, Work, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }

    sealed interface ListenState {
        data object Hide : ListenState
        data object Unknown : ListenState
        data class Known(
            val listenCount: Long = 0,
        ) : ListenState
    }
}
