package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
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
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.component.MultipleChoiceBottomSheet
import ly.david.musicsearch.ui.common.dialog.SimpleAlertDialog
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.clearHistory
import musicsearch.ui.common.generated.resources.deleteLookupHistoryConfirmation
import musicsearch.ui.common.generated.resources.no
import musicsearch.ui.common.generated.resources.recentHistory
import musicsearch.ui.common.generated.resources.sortAction
import musicsearch.ui.common.generated.resources.yes
import org.jetbrains.compose.resources.stringResource
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SwallowedException")
@Composable
internal fun HistoryUi(
    state: HistoryUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            title = stringResource(Res.string.deleteLookupHistoryConfirmation),
            confirmText = stringResource(Res.string.yes),
            dismissText = stringResource(Res.string.no),
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirmClick = {
                coroutineScope.launch {
                    eventSink(HistoryUiEvent.MarkAllHistoryForDeletion)

                    try {
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

                            SnackbarResult.Dismissed -> {
                                eventSink(HistoryUiEvent.DeleteAllHistory)
                            }
                        }
                    } catch (ex: CancellationException) {
                        eventSink(HistoryUiEvent.DeleteAllHistory)
                    }
                }
            },
        )
    }

    if (showBottomSheet) {
        MultipleChoiceBottomSheet(
            options = HistorySortOption.entries.map { it.getLabel() },
            selectedOptionIndex = state.sortOption.ordinal,
            onSortOptionIndexClick = {
                eventSink(HistoryUiEvent.UpdateSortOption(it))
            },
            bottomSheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false },
        )
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(HistoryUiEvent.NavigateUp)
                },
                title = stringResource(Res.string.recentHistory),
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.clearHistory)) },
                        onClick = {
                            showDeleteConfirmationDialog = true
                            closeMenu()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.sortAction)) },
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
        val lazyPagingItems = state.pagingDataFlow.collectAsLazyPagingItems()
        HistoryUi(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyListState = state.lazyListState,
            onItemClick = { entity, id ->
                eventSink(
                    HistoryUiEvent.ClickItem(
                        entityType = entity,
                        id = id,
                    ),
                )
            },
            onDeleteItem = { history ->
                coroutineScope.launch {
                    eventSink(HistoryUiEvent.MarkHistoryForDeletion(history))

                    try {
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
                    } catch (ex: CancellationException) {
                        eventSink(HistoryUiEvent.DeleteHistory(history))
                    }
                }
            },
        )
    }
}

@Composable
internal fun HistoryUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    onDeleteItem: (LookupHistoryListItemModel) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is LookupHistoryListItemModel -> {
                HistoryListItem(
                    lookupHistory = listItemModel,
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
