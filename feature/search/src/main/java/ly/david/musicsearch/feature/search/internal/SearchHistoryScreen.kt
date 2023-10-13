package ly.david.musicsearch.feature.search.internal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.Header
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.strings.LocalStrings
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
    val strings = LocalStrings.current
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            title = strings.deleteSearchHistoryConfirmation,
            confirmText = strings.yes,
            dismissText = strings.no,
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirmClick = { onDeleteAllHistory() }
        )
    }

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        customNoResultsText = "Enter a query to start searching MusicBrainz's database",
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is Header -> {
                RecentSearchesHeader(
                    onDeleteAllHistory = {
                        showDeleteConfirmationDialog = true
                    },
                )
            }

            is SearchHistoryListItemModel -> {
                SearchHistoryListItem(
                    searchHistory = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = onItemClick,
                    onDeleteItem = onDeleteItem,
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
