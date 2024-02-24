package ly.david.musicsearch.shared.feature.history.internal

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface HistoryUiEvent : CircuitUiEvent {
    data class UpdateQuery(val query: String) : HistoryUiEvent
    data class UpdateSortOption(val sortOption: HistorySortOption) : HistoryUiEvent
    data class MarkHistoryForDeletion(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data class UnMarkHistoryForDeletion(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data class DeleteHistory(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data object MarkAllHistoryForDeletion : HistoryUiEvent
    data object UnMarkAllHistoryForDeletion : HistoryUiEvent
    data object DeleteAllHistory : HistoryUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : HistoryUiEvent
}
