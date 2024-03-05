package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel

@Stable
internal data class CollectionUiState(
    val collection: CollectionListItemModel?,
    val actionableResult: ActionableResult?,
    val query: String,
    val showMoreInfoInReleaseListItem: Boolean,
    val sortReleaseGroupListItems: Boolean,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (CollectionUiEvent) -> Unit,
) : CircuitUiState
