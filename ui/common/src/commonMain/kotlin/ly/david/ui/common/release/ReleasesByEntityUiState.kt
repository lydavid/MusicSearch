package ly.david.ui.common.release

import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel

data class ReleasesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val showMoreInfo: Boolean,
    val eventSink: (ReleasesByEntityUiEvent) -> Unit,
) : CircuitUiState
