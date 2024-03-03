package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.dialog.SimpleAlertDialog
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun History(
    state: HistoryUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            title = strings.deleteLookupHistoryConfirmation,
            confirmText = strings.yes,
            dismissText = strings.no,
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirmClick = {
                scope.launch {
                    eventSink(HistoryUiEvent.MarkAllHistoryForDeletion)
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Cleared history",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            eventSink(HistoryUiEvent.UnMarkAllHistoryForDeletion)
                        }

                        // TODO: leaving the screen before this is executed marks all item as deleted,
                        //  so they won't show in the UI, but when the user next undo a delete all,
                        //  they will recover all of those marked as deleted, including some possibly old entries
                        //  Wikipedia doesn't offer an undo for delete all, so maybe consider this a hidden feature?
                        SnackbarResult.Dismissed -> {
                            eventSink(HistoryUiEvent.DeleteAllHistory)
                        }
                    }
                }
            },
        )
    }

    if (showBottomSheet) {
        HistorySortBottomSheet(
            sortOption = state.sortOption,
            onSortOptionClick = {
                eventSink(HistoryUiEvent.UpdateSortOption(it))
            },
            bottomSheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.recentHistory,
                scrollBehavior = scrollBehavior,
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(HistoryUiEvent.UpdateQuery(it))
                },
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(strings.clearHistory) },
                        onClick = {
                            showDeleteConfirmationDialog = true
                            closeMenu()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(strings.sort) },
                        onClick = {
                            showBottomSheet = true
                            closeMenu()
                        },
                    )
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        History(
            lazyPagingItems = state.lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onItemClick = { entity, id, title ->
                eventSink(
                    HistoryUiEvent.ClickItem(
                        entity = entity,
                        id = id,
                        title = title,
                    ),
                )
            },
            onDeleteItem = { history ->
                scope.launch {
                    eventSink(HistoryUiEvent.MarkHistoryForDeletion(history))

                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Removed ${history.title}",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            eventSink(HistoryUiEvent.UnMarkHistoryForDeletion(history))
                        }

                        SnackbarResult.Dismissed -> {
                            eventSink(HistoryUiEvent.DeleteHistory(history))
                        }
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun History(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryListItemModel) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is LookupHistoryListItemModel -> {
                HistoryListItem(
                    lookupHistory = listItemModel,
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