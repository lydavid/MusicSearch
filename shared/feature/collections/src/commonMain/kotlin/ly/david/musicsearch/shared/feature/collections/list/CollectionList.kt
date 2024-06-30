package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BasicDialogOverlay
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.shared.feature.collections.components.CreateNewCollectionDialogContent
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter

/**
 * Displays a list of all of your collections.
 */
@Composable
internal fun CollectionList(
    state: CollectionListUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    CollectionList(
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
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
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
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                        showLocal = showLocal,
                        onShowLocalToggle = onShowLocalToggle,
                        showRemote = showRemote,
                        onShowRemoteToggle = onShowRemoteToggle,
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
                        modifier = Modifier.animateItemPlacement(),
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

@Composable
private fun CollectionsFilterChipsBar(
    modifier: Modifier = Modifier,
    showLocal: Boolean = true,
    onShowLocalToggle: (Boolean) -> Unit = {},
    showRemote: Boolean = true,
    onShowRemoteToggle: (Boolean) -> Unit = {},
) {
    Row(modifier = modifier.padding(horizontal = 16.dp)) {
        FilterChip(
            selected = showLocal,
            onClick = { onShowLocalToggle(!showLocal) },
            label = { Text(text = "Local") },
            leadingIcon = {
                if (showLocal) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        FilterChip(
            selected = showRemote,
            onClick = { onShowRemoteToggle(!showRemote) },
            label = { Text(text = "Remote") },
            leadingIcon = {
                if (showRemote) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                    )
                }
            },
        )
    }
}
