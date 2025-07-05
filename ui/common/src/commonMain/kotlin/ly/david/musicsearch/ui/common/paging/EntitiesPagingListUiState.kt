package ly.david.musicsearch.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

data class EntitiesPagingListUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState,
    val showMoreInfo: Boolean = true,
)
