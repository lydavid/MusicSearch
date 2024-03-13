package ly.david.ui.common.release

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel

@Stable
data class ReleasesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val showMoreInfo: Boolean,
    val eventSink: (ReleasesByEntityUiEvent) -> Unit,
) : CircuitUiState
