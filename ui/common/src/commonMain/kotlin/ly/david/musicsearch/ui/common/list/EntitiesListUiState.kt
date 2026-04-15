package ly.david.musicsearch.ui.common.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.list.SortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

/**
 * After [pagingDataFlow] is collected, we use [ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState] instead.
 */
@Stable
data class EntitiesListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val lazyListState: LazyListState = LazyListState(),
    val totalCount: Int = 0,
    val filteredCount: Int = 0,
    val sortOption: SortOption = SortOption.None,
    val eventSink: (EntitiesListUiEvent) -> Unit = {},
) : CircuitUiState
