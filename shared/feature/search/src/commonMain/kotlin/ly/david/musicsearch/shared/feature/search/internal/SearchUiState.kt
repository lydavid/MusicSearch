package ly.david.musicsearch.shared.feature.search.internal

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@Stable
internal data class SearchUiState(
    val query: String,
    val entity: MusicBrainzEntity,
    val searchResults: LazyPagingItems<ListItemModel>,
    val searchResultsListState: LazyListState = LazyListState(),
    val searchHistory: LazyPagingItems<ListItemModel>,
    val searchHistoryListState: LazyListState = LazyListState(),
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState
