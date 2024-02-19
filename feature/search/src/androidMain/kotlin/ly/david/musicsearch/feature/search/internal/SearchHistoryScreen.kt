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
import ly.david.musicsearch.core.models.listitem.Header
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.dialog.SimpleAlertDialog
import ly.david.ui.commonlegacy.paging.ScreenWithPagingLoadingAndError

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
            onConfirmClick = { onDeleteAllHistory() },
        )
    }

    ScreenWithPagingLoadingAndError(
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
