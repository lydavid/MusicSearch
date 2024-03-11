package ly.david.ui.common.relation

import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class RelationsUiState(
    val lazyPagingItems: LazyPagingItems<RelationListItemModel>,
    val eventSink: (RelationsUiEvent) -> Unit,
) : CircuitUiState
