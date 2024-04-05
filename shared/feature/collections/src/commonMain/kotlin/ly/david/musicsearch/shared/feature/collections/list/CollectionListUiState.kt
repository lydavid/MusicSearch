package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel

@Stable
internal data class CollectionListUiState(
    val query: String,
    val showLocal: Boolean,
    val showRemote: Boolean,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val eventSink: (CollectionListUiEvent) -> Unit,
) : CircuitUiState
