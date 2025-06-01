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
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.EditToggle
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
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
@Suppress("SwallowedException", "CyclomaticComplexMethod")
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
) {
    val collection = state.collection
    val entity = collection?.entity
    val eventSink = state.eventSink
    val suspendEventSink = state.suspendEventSink
    val loginEventSink = state.loginUiState.eventSink
    val releasesEventSink = state.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsListUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val areasLazyPagingItems = state.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems = state.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems = state.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems = state.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems = state.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems = state.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems = state.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems = state.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems = state.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems = state.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems = state.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()

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
                        eventSink(CollectionUiEvent.UnMarkItemsAsDeleted)
                    }

                    SnackbarResult.Dismissed -> {
                        suspendEventSink(SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted)
                    }
                }
            } catch (_: CancellationException) {
                suspendEventSink(SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted)
            }
        }
    }
    state.secondActionableResult?.let { result ->
        LaunchedEffect(result) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = result.message,
                actionLabel = result.action?.name,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
            )

            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    loginEventSink(LoginUiEvent.StartLogin)
                }

                SnackbarResult.Dismissed -> {
                    // no-op
                }
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
                    RefreshMenuItem(
                        onClick = {
                            when (entity) {
                                MusicBrainzEntity.AREA -> areasLazyPagingItems.refresh()
                                MusicBrainzEntity.ARTIST -> artistsLazyPagingItems.refresh()
                                MusicBrainzEntity.EVENT -> eventsLazyPagingItems.refresh()
                                MusicBrainzEntity.GENRE -> genresLazyPagingItems.refresh()
                                MusicBrainzEntity.INSTRUMENT -> instrumentsLazyPagingItems.refresh()
                                MusicBrainzEntity.LABEL -> labelsLazyPagingItems.refresh()
                                MusicBrainzEntity.PLACE -> placesLazyPagingItems.refresh()
                                MusicBrainzEntity.RECORDING -> recordingsLazyPagingItems.refresh()
                                MusicBrainzEntity.RELEASE -> releasesLazyPagingItems.refresh()
                                MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsLazyPagingItems.refresh()
                                MusicBrainzEntity.SERIES -> seriesLazyPagingItems.refresh()
                                MusicBrainzEntity.WORK -> worksLazyPagingItems.refresh()
                                else -> {
                                    // no-op
                                }
                            }
                        },
                    )
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            url = state.url,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (entity == MusicBrainzEntity.RELEASE) {
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
                        lazyPagingItems = areasLazyPagingItems,
                        lazyListState = state.areasListUiState.lazyListState,
                    )
                }

                MusicBrainzEntity.ARTIST -> {
                    EntitiesListUiState(
                        lazyListState = state.artistsListUiState.lazyListState,
                        lazyPagingItems = artistsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.EVENT -> {
                    EntitiesListUiState(
                        lazyListState = state.eventsListUiState.lazyListState,
                        lazyPagingItems = eventsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.GENRE -> {
                    EntitiesListUiState(
                        lazyListState = state.genresListUiState.lazyListState,
                        lazyPagingItems = genresLazyPagingItems,
                    )
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    EntitiesListUiState(
                        lazyListState = state.instrumentsListUiState.lazyListState,
                        lazyPagingItems = instrumentsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.LABEL -> {
                    EntitiesListUiState(
                        lazyListState = state.labelsListUiState.lazyListState,
                        lazyPagingItems = labelsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.PLACE -> {
                    EntitiesListUiState(
                        lazyListState = state.placesListUiState.lazyListState,
                        lazyPagingItems = placesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RECORDING -> {
                    EntitiesListUiState(
                        lazyListState = state.recordingsListUiState.lazyListState,
                        lazyPagingItems = recordingsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RELEASE -> {
                    EntitiesListUiState(
                        lazyListState = state.releasesListUiState.lazyListState,
                        lazyPagingItems = releasesLazyPagingItems,
                        showMoreInfo = state.releasesListUiState.showMoreInfo,
                    )
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    EntitiesListUiState(
                        lazyListState = state.releaseGroupsListUiState.lazyListState,
                        lazyPagingItems = releaseGroupsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.SERIES -> {
                    EntitiesListUiState(
                        lazyListState = state.seriesListUiState.lazyListState,
                        lazyPagingItems = seriesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.WORK -> {
                    EntitiesListUiState(
                        lazyListState = state.worksListUiState.lazyListState,
                        lazyPagingItems = worksLazyPagingItems,
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
