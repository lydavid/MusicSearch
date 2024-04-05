package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.core.models.listitem.ListItemModel

@Stable
internal data class HistoryUiState(
    val query: String,
    val sortOption: HistorySortOption,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (HistoryUiEvent) -> Unit,
) : CircuitUiState
