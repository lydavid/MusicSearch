package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.PagingData
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

@Stable
data class RelationsUiState(
    val pagingDataFlow: Flow<PagingData<RelationListItemModel>> = emptyFlow(),
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (RelationsUiEvent) -> Unit = {},
) : CircuitUiState
