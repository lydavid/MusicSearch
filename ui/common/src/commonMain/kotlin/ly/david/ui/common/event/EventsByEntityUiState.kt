package ly.david.ui.common.event

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.EventListItemModel

@Stable
data class EventsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<EventListItemModel>,
    val eventSink: (EventsByEntityUiEvent) -> Unit,
) : CircuitUiState
