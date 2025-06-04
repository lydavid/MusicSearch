package ly.david.musicsearch.ui.common.list

import androidx.compose.foundation.lazy.LazyListState
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

// TODO: rename
data class EntitiesListUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState,
    val showMoreInfo: Boolean = true,
)
