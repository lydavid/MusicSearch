package ly.david.musicsearch.shared.feature.collections.single

import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel

internal data class CollectionUiState(
    val collection: CollectionListItemModel?,
    val query: String,
//    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (CollectionUiEvent) -> Unit,
) : CircuitUiState
