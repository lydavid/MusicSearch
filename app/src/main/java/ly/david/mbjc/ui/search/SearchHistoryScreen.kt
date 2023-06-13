package ly.david.mbjc.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.listitem.Header
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.SearchHistoryListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchHistoryScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzResource, query: String) -> Unit = { _, _ -> },
    onDeleteItem: (SearchHistoryListItemModel) -> Unit = {},
    onDeleteAllHistory: () -> Unit = {}
) {
    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is Header -> {
                RecentSearchesHeader(onDeleteAllHistory = onDeleteAllHistory)
            }

            is SearchHistoryListItemModel -> {
                SearchHistoryListItem(
                    searchHistory = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = onItemClick,
                    onDeleteItem = onDeleteItem
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
