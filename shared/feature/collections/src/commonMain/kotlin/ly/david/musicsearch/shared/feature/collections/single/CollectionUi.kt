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
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUiState
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
    val releasesEventSink = state.entitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.entitiesListUiState.releaseGroupsListUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val areasLazyPagingItems =
        state.entitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems =
        state.entitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems =
        state.entitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems =
        state.entitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems =
        state.entitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems =
        state.entitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems =
        state.entitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.entitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.entitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems =
        state.entitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems =
        state.entitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems =
        state.entitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()

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
                            toggled = state.entitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.entitiesListUiState.releasesListUiState.showMoreInfo,
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
                    EntitiesPagingListUiState(
                        lazyPagingItems = areasLazyPagingItems,
                        lazyListState = state.entitiesListUiState.areasListUiState.lazyListState,
                    )
                }

                MusicBrainzEntity.ARTIST -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.artistsListUiState.lazyListState,
                        lazyPagingItems = artistsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.EVENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.eventsListUiState.lazyListState,
                        lazyPagingItems = eventsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.GENRE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.genresListUiState.lazyListState,
                        lazyPagingItems = genresLazyPagingItems,
                    )
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.instrumentsListUiState.lazyListState,
                        lazyPagingItems = instrumentsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.LABEL -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.labelsListUiState.lazyListState,
                        lazyPagingItems = labelsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.PLACE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.placesListUiState.lazyListState,
                        lazyPagingItems = placesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RECORDING -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.recordingsListUiState.lazyListState,
                        lazyPagingItems = recordingsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RELEASE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.releasesListUiState.lazyListState,
                        lazyPagingItems = releasesLazyPagingItems,
                        showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                    )
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.releaseGroupsListUiState.lazyListState,
                        lazyPagingItems = releaseGroupsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.SERIES -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.seriesListUiState.lazyListState,
                        lazyPagingItems = seriesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.WORK -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.entitiesListUiState.worksListUiState.lazyListState,
                        lazyPagingItems = worksLazyPagingItems,
                    )
                }

                else -> {
                    error("$entity is not supported for collections.")
                }
            }
            EntitiesPagingListUi(
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
