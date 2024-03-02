package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuitx.overlays.BasicDialogOverlay
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CreateCollectionDialogContent
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter

// TODO: hoist and preview

/**
 * Displays a list of all of your collections.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun CollectionList(
    state: CollectionListUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.collections,
                scrollBehavior = scrollBehavior,
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(CollectionListUiEvent.UpdateQuery(it))
                },
                additionalActions = {
                    IconButton(onClick = {
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
                                    CreateCollectionDialogContent(
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
                            eventSink(CollectionListUiEvent.CreateCollection(result))
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = strings.createCollection,
                        )
                    }
                },
                additionalBar = {
                    CollectionsFilterChipsBar(
                        showLocal = state.showLocal,
                        onShowLocalToggle = {
                            eventSink(CollectionListUiEvent.UpdateShowLocal(it))
                        },
                        showRemote = state.showRemote,
                        onShowRemoteToggle = {
                            eventSink(CollectionListUiEvent.UpdateShowRemote(it))
                        },
                    )
                },
            )
        },
    ) { innerPadding ->

        ScreenWithPagingLoadingAndError(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyPagingItems = state.lazyPagingItems,
        ) { collectionListItemModel: CollectionListItemModel? ->
            when (collectionListItemModel) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collection = collectionListItemModel,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = {
                            eventSink(
                                CollectionListUiEvent.ClickCollection(
                                    id = id,
                                ),
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
