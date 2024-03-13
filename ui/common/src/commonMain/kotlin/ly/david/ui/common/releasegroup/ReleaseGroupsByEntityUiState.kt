package ly.david.ui.common.releasegroup

import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.ListItemModel

data class ReleaseGroupsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val sort: Boolean,
    val eventSink: (ReleaseGroupsByEntityUiEvent) -> Unit,
) : CircuitUiState
