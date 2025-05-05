package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DeleteOutline
import ly.david.musicsearch.ui.common.list.EntitiesListUi
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.EditToggle
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings
import kotlin.coroutines.cancellation.CancellationException

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SwallowedException")
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
) {
    val collection = state.collection
    val eventSink = state.eventSink
    val suspendEventSink = state.suspendEventSink
    val releasesEventSink = state.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsListUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO: handle second actionableResult or else we will keep looping
    state.actionableResult?.let { result ->
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
                        eventSink(CollectionUiEvent.UnMarkItemsAsDeleted)
                    }

                    SnackbarResult.Dismissed -> {
                        suspendEventSink(SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted)
                    }
                }
            } catch (ex: CancellationException) {
                suspendEventSink(SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = state.title,
                scrollBehavior = scrollBehavior,
                additionalActions = {
                    IconButton(onClick = {
                        state.topAppBarEditState.toggleEditMode()
                    }) {
                        EditToggle(state.topAppBarEditState)
                    }
                },
                overflowDropdownMenuItems = {
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            url = state.url,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (collection?.entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (collection?.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                    if (state.selectedIds.isNotEmpty()) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete ${state.selectedIds.size}",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = CustomIcons.DeleteOutline,
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                closeMenu()
                                eventSink(CollectionUiEvent.MarkSelectedItemsAsDeleted)
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                topAppBarEditState = state.topAppBarEditState,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding),
            )
        } else {
            val uiState = when (val entity = collection.entity) {
                MusicBrainzEntity.AREA -> {
                    EntitiesListUiState(
                        lazyListState = state.areasListUiState.lazyListState,
                        lazyPagingItems = state.areasListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                    )
                }

                MusicBrainzEntity.ARTIST -> {
                    EntitiesListUiState(
                        lazyListState = state.artistsListUiState.lazyListState,
                        lazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                    )
                }

                MusicBrainzEntity.EVENT -> {
                    EntitiesListUiState(
                        lazyListState = state.eventsListUiState.lazyListState,
                        lazyPagingItems = state.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                    )
                }

                MusicBrainzEntity.GENRE -> {
                    EntitiesListUiState(
                        lazyListState = state.genresListUiState.lazyListState,
                        lazyPagingItems = state.genresListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    EntitiesListUiState(
                        lazyListState = state.instrumentsListUiState.lazyListState,
                        lazyPagingItems = state.instrumentsListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.LABEL -> {
                    EntitiesListUiState(
                        lazyListState = state.labelsListUiState.lazyListState,
                        lazyPagingItems = state.labelsListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.PLACE -> {
                    EntitiesListUiState(
                        lazyListState = state.placesListUiState.lazyListState,
                        lazyPagingItems = state.placesListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.RECORDING -> {
                    EntitiesListUiState(
                        lazyListState = state.recordingsListUiState.lazyListState,
                        lazyPagingItems = state.recordingsListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.RELEASE -> {
                    EntitiesListUiState(
                        lazyListState = state.releasesListUiState.lazyListState,
                        lazyPagingItems = state.releasesListUiState.lazyPagingItems,
                        showMoreInfo = state.releasesListUiState.showMoreInfo,
                    )
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    EntitiesListUiState(
                        lazyListState = state.releaseGroupsListUiState.lazyListState,
                        lazyPagingItems = state.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                    )
                }

                MusicBrainzEntity.SERIES -> {
                    EntitiesListUiState(
                        lazyListState = state.seriesListUiState.lazyListState,
                        lazyPagingItems = state.seriesListUiState.lazyPagingItems,
                    )
                }

                MusicBrainzEntity.WORK -> {
                    EntitiesListUiState(
                        lazyListState = state.worksListUiState.lazyListState,
                        lazyPagingItems = state.worksListUiState.lazyPagingItems,
                    )
                }

                else -> {
                    error("$entity is not supported for collections.")
                }
            }
            EntitiesListUi(
                uiState = uiState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
                selectedIds = state.selectedIds,
                onItemClick = { entity, id, title ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entity = entity,
                            id = id,
                            title = title,
                        ),
                    )
                },
                onSelect = {
                    eventSink(
                        CollectionUiEvent.ToggleSelectItem(
                            collectableId = it,
                        ),
                    )
                },
                requestForMissingCoverArtUrl = { entityId ->
                    when (collection.entity) {
                        MusicBrainzEntity.RELEASE -> {
                            releasesEventSink(ReleasesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            releaseGroupsEventSink(ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        else -> {
                            // no-op
                        }
                    }
                },
            )
        }
    }
}
