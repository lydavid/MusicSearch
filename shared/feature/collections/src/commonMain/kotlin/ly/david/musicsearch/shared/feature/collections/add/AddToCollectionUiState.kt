package ly.david.musicsearch.shared.feature.collections.add

import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel

internal data class AddToCollectionUiState(
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val eventSink: (AddToCollectionUiEvent) -> Unit,
) : CircuitUiState
