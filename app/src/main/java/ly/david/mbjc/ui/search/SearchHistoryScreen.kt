package ly.david.mbjc.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.listitem.Header
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.SearchHistoryListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.R
import ly.david.ui.common.dialog.SimpleAlertDialog
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchHistoryScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, query: String) -> Unit = { _, _ -> },
    onDeleteItem: (SearchHistoryListItemModel) -> Unit = {},
    onDeleteAllHistory: () -> Unit = {},
) {
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            title = stringResource(id = R.string.delete_search_history_confirmation),
            confirmText = stringResource(id = R.string.yes),
            dismissText = stringResource(id = R.string.no),
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirmClick = { onDeleteAllHistory() }
        )
    }

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is Header -> {
                RecentSearchesHeader(
                    isListEmpty = listItemModel.isListEmpty,
                    onDeleteAllHistory = {
                        showDeleteConfirmationDialog = true
                    }
                )
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
