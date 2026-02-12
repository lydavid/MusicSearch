package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BasicDialogOverlay
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.shared.feature.collections.create.CreateNewCollectionDialogContent
import ly.david.musicsearch.ui.common.icons.Add
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.DeleteMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.collections
import musicsearch.ui.common.generated.resources.createCollection
import org.jetbrains.compose.resources.stringResource
import kotlin.coroutines.cancellation.CancellationException

/**
 * Displays a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionListUi(
    state: CollectionsListUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (showBottomSheet) {
        CollectionSortBottomSheet(
            sortOption = state.sortOption,
            onSortOptionClick = {
                eventSink(CollectionsListUiEvent.UpdateSortOption(it))
            },
            onDismiss = { showBottomSheet = false },
        )
    }

    CollectionListUi(
        state = state,
        modifier = modifier,
        onSortClick = {
            showBottomSheet = true
        },
        onCreateCollectionClick = {
            scope.launch {
                val basicDialogOverlay: BasicDialogOverlay<Unit, CreateNewCollectionResult> = BasicDialogOverlay(
                    model = Unit,
                    onDismissRequest = {
                        // The result must not be null, so we use a sealed interface.
                        CreateNewCollectionResult.Dismissed
                    },
                ) { _, overlayNavigator ->
                    CreateNewCollectionDialogContent(
                        onDismiss = { overlayNavigator.finish(CreateNewCollectionResult.Dismissed) },
                        onSubmit = { name, entity ->
                            overlayNavigator.finish(
                                CreateNewCollectionResult.NewCollection(
                                    name = name,
                                    entity = entity,
                                ),
                            )
                        },
                    )
                }
                val result: CreateNewCollectionResult = overlayHost.show(basicDialogOverlay)
                if (result is CreateNewCollectionResult.NewCollection) {
                    eventSink(CollectionsListUiEvent.CreateNewCollection(result))
                }
            }
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CollectionListUi(
    state: CollectionsListUiState,
    modifier: Modifier = Modifier,
    onSortClick: () -> Unit = {},
    onCreateCollectionClick: () -> Unit = {},
) {
    val eventSink = state.eventSink
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    state.firstActionableResult?.let { result ->
        LaunchedEffect(result) {
            try {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = result.message,
                    actionLabel = result.action?.name,
                    duration = SnackbarDuration.Short,
                    withDismissAction = true,
                )

                when (snackbarResult) {
                    SnackbarResult.ActionPerformed -> {
                        eventSink(CollectionsListUiEvent.UnMarkItemsAsDeleted)
                    }

                    SnackbarResult.Dismissed -> {
                        eventSink(CollectionsListUiEvent.DeleteItemsMarkedAsDeleted)
                    }
                }
            } catch (_: CancellationException) {
                eventSink(CollectionsListUiEvent.DeleteItemsMarkedAsDeleted)
            }
        }
    }
    state.secondActionableResult?.let { result ->
        LaunchedEffect(result) {
            snackbarHostState.showSnackbar(
                message = result.message,
                actionLabel = result.action?.name,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
            )
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = stringResource(Res.string.collections),
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
                additionalActions = {
                    IconButton(onClick = onCreateCollectionClick) {
                        Icon(
                            imageVector = CustomIcons.Add,
                            contentDescription = stringResource(Res.string.createCollection),
                        )
                    }
                },
                overflowDropdownMenuItems = {
                    RefreshMenuItem(
                        onClick = { state.lazyPagingItems.refresh() },
                    )
                    if (state.selectionState.selectedIds.isNotEmpty()) {
                        DeleteMenuItem(
                            selectionState = state.selectionState,
                            onClick = {
                                eventSink(CollectionsListUiEvent.MarkSelectedItemsAsDeleted)
                            },
                        )
                    }
                },
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        ids = state.lazyPagingItems.itemSnapshotList.items
                            .filterNot { item -> item.isRemote }
                            .map { item -> item.id },
                    )
                },
                additionalBar = {
                    CollectionsFilterChipsBar(
                        sortOption = state.sortOption,
                        showLocal = state.showLocal,
                        onShowLocalToggle = {
                            eventSink(CollectionsListUiEvent.UpdateShowLocal(it))
                        },
                        showRemote = state.showRemote,
                        onShowRemoteToggle = {
                            eventSink(CollectionsListUiEvent.UpdateShowRemote(it))
                        },
                        onSortClick = onSortClick,
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

        ScreenWithPagingLoadingAndError(
            lazyPagingItems = state.lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyListState = state.lazyListState,
            keyed = true,
        ) { collectionListItemModel: CollectionListItemModel? ->
            when (collectionListItemModel) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collection = collectionListItemModel,
                        query = state.topAppBarFilterState.filterText,
                        onClick = {
                            eventSink(CollectionsListUiEvent.GoToCollection(id = collectionListItemModel.id))
                        },
                        enabled = !state.selectionState.isEditMode || !collectionListItemModel.isRemote,
                        isSelected = state.selectionState.selectedIds.contains(collectionListItemModel.id),
                        onSelect = {
                            state.selectionState.toggleSelection(
                                id = it,
                                totalLoadedCount = state.lazyPagingItems.itemSnapshotList.items
                                    .filterNot { item -> item.isRemote }
                                    .map { item -> item.id }.size,
                            )
                        },
                    )
                }

                else -> {
                    // Do nothing.
                }
            }
        }
    }
}
