package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

@Stable
data class ReleaseGroupsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val sort: Boolean,
    val eventSink: (ReleaseGroupsByEntityUiEvent) -> Unit,
) : CircuitUiState
