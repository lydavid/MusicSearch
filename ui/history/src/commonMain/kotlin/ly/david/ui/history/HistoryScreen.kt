package ly.david.ui.history

import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.core.parcelize.CommonParcelize

@CommonParcelize
data object HistoryScreen : Screen {
    data class UiState(
        val query: String,
        val sortOption: HistorySortOption,
        val lazyPagingItems: LazyPagingItems<ListItemModel>,
        val eventSink: (UiEvent) -> Unit,
    ) : CircuitUiState

    sealed interface UiEvent : CircuitUiEvent {
        data class UpdateQuery(val query: String) : UiEvent
        data class UpdateSortOption(val sortOption: HistorySortOption) : UiEvent
        data class MarkHistoryForDeletion(val history: LookupHistoryListItemModel) : UiEvent
        data class UnMarkHistoryForDeletion(val history: LookupHistoryListItemModel) : UiEvent
        data class DeleteHistory(val history: LookupHistoryListItemModel) : UiEvent
        data object MarkAllHistoryForDeletion : UiEvent
        data object UnMarkAllHistoryForDeletion : UiEvent
        data object DeleteAllHistory : UiEvent
        data class OpenItem(val id: String) : UiEvent
    }
}
