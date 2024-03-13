package ly.david.ui.common.relation

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

@Stable
data class RelationsUiState(
    val lazyPagingItems: LazyPagingItems<RelationListItemModel>,
    val eventSink: (RelationsUiEvent) -> Unit,
) : CircuitUiState
