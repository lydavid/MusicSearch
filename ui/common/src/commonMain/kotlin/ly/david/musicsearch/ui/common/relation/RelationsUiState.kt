package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

@Stable
data class RelationsUiState(
    val lazyPagingItems: LazyPagingItems<RelationListItemModel>,
    val relationsLazyListState: LazyListState = LazyListState(),
    val eventSink: (RelationsUiEvent) -> Unit = {},
) : CircuitUiState
