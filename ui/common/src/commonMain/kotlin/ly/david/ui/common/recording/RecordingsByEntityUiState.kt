package ly.david.ui.common.recording

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel

@Stable
data class RecordingsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    val eventSink: (RecordingsByEntityUiEvent) -> Unit,
) : CircuitUiState
