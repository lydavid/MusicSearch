package ly.david.musicsearch.ui.common.place

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel

@Stable
data class PlacesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val eventSink: (PlacesByEntityUiEvent) -> Unit = {},
) : CircuitUiState
