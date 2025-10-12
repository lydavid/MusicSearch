package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.Header
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.dialog.SimpleAlertDialog
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun SearchHistoryUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entityType: MusicBrainzEntityType, query: String) -> Unit = { _, _ -> },
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
