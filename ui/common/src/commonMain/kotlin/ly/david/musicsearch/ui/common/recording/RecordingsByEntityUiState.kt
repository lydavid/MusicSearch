package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel

@Stable
data class RecordingsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (RecordingsByEntityUiEvent) -> Unit,
) : CircuitUiState
