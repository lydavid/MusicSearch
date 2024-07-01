package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BasicDialogOverlay
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.collection.CollectionSortOption
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.shared.feature.collections.create.CreateNewCollectionDialogContent
import ly.david.musicsearch.shared.feature.collections.create.NewCollection
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings

/**
 * Displays a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionList(
    state: CollectionListUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        CollectionSortBottomSheet(
            sortOption = state.sortOption,
            onSortOptionClick = {
                eventSink(CollectionListUiEvent.UpdateSortOption(it))
            },
            bottomSheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false },
        )
    }

    CollectionList(
        sortOption = state.sortOption,
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        filterText = state.query,
        onFilterTextChange = { eventSink(CollectionListUiEvent.UpdateQuery(it)) },
        onCreateCollectionClick = {
            scope.launch {
                val result = overlayHost.show(
                    BasicDialogOverlay(
                        model = Unit,
                        onDismissRequest = {
                            // This is expecting a non-null Result,
                            // so a dismissal emits this with all null arguments
                            NewCollection()
                        },
                    ) { _, overlayNavigator ->
                        CreateNewCollectionDialogContent(
                            onDismiss = { overlayNavigator.finish(NewCollection()) },
                            onSubmit = { name, entity ->
                                overlayNavigator.finish(
                                    NewCollection(
                                        name,
                                        entity,
                                    ),
                                )
                            },
                        )
                    },
                )
                eventSink(CollectionListUiEvent.CreateNewCollection(result))
            }
        },
        showLocal = state.showLocal,
        onShowLocalToggle = {
            eventSink(CollectionListUiEvent.UpdateShowLocal(it))
        },
        showRemote = state.showRemote,
        onShowRemoteToggle = {
            eventSink(CollectionListUiEvent.UpdateShowRemote(it))
        },
        onCollectionClick = {
            eventSink(CollectionListUiEvent.GoToCollection(it))
        },
        onSortClick = {
            showBottomSheet = true
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CollectionList(
    lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    onCreateCollectionClick: () -> Unit = {},
    showLocal: Boolean = true,
    onShowLocalToggle: (Boolean) -> Unit = {},
    showRemote: Boolean = true,
    onShowRemoteToggle: (Boolean) -> Unit = {},
    onCollectionClick: (String) -> Unit = {},
    sortOption: CollectionSortOption = CollectionSortOption.ALPHABETICALLY,
    onSortClick: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.collections,
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = onFilterTextChange,
                additionalActions = {
                    IconButton(onClick = onCreateCollectionClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = strings.createCollection,
                        )
                    }
                },
                additionalBar = {
                    CollectionsFilterChipsBar(
                        sortOption = sortOption,
                        showLocal = showLocal,
                        onShowLocalToggle = onShowLocalToggle,
                        showRemote = showRemote,
                        onShowRemoteToggle = onShowRemoteToggle,
                        onSortClick = onSortClick,
                    )
                },
            )
        },
    ) { innerPadding ->

        ScreenWithPagingLoadingAndError(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyPagingItems = lazyPagingItems,
        ) { collectionListItemModel: CollectionListItemModel? ->
            when (collectionListItemModel) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collection = collectionListItemModel,
                        // TODO: animateItemPlacement messes up UI in emulator
//                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onCollectionClick(id) },
                    )
                }

                else -> {
                    // Do nothing.
                }
            }
        }
    }
}
