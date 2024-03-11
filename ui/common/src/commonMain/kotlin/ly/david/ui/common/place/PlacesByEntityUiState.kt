package ly.david.ui.common.place

import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel

data class PlacesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val eventSink: (PlacesByEntityUiEvent) -> Unit,
) : CircuitUiState
