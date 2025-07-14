package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.getLazyPagingItemsForTab
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.DeleteMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.SortToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab
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
    val releasesEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.allEntitiesListUiState.releaseGroupsListUiState.eventSink
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val overlayHost = LocalOverlayHost.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val areasLazyPagingItems =
        state.allEntitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems =
        state.allEntitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems =
        state.allEntitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems =
        state.allEntitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems =
        state.allEntitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems =
        state.allEntitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems =
        state.allEntitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.allEntitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.allEntitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems =
        state.allEntitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems =
        state.allEntitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems =
        state.allEntitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val unusedLazyPagingItems =
        flowOf(PagingData.from(listOf<ListItemModel>())).collectAsLazyPagingItems()
    val entitiesLazyPagingItems = EntitiesLazyPagingItems(
        areasLazyPagingItems = areasLazyPagingItems,
        artistsLazyPagingItems = artistsLazyPagingItems,
        eventsLazyPagingItems = eventsLazyPagingItems,
        genresLazyPagingItems = genresLazyPagingItems,
        instrumentsLazyPagingItems = instrumentsLazyPagingItems,
        labelsLazyPagingItems = labelsLazyPagingItems,
        placesLazyPagingItems = placesLazyPagingItems,
        recordingsLazyPagingItems = recordingsLazyPagingItems,
        releasesLazyPagingItems = releasesLazyPagingItems,
        releaseGroupsLazyPagingItems = releaseGroupsLazyPagingItems,
        seriesLazyPagingItems = seriesLazyPagingItems,
        worksLazyPagingItems = worksLazyPagingItems,
        relationsLazyPagingItems = unusedLazyPagingItems,
        tracksLazyPagingItems = unusedLazyPagingItems,
    )

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
                overflowDropdownMenuItems = {
                    entity?.toTab()?.let { tab ->
                        RefreshMenuItem(
                            tab = tab,
                            onClick = {
                                entitiesLazyPagingItems.getLazyPagingItemsForTab(tab)?.refresh()
                            },
                        )
                    }
                    StatsMenuItem(
                        statsScreen = StatsScreen(
                            byEntityId = collection?.id.orEmpty(),
                            byEntity = MusicBrainzEntity.COLLECTION,
                            tabs = listOfNotNull(entity?.toTab()).toPersistentList(),
                            isRemote = collection?.isRemote == true,
                        ),
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                    )
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            url = state.url,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                        SortToggleMenuItem(
                            sorted = state.allEntitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(EntitiesListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (entity == MusicBrainzEntity.RELEASE) {
                        MoreInfoToggleMenuItem(
                            showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                    AddAllToCollectionMenuItem(
                        tab = entity?.toTab(),
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(LoginUiEvent.StartLogin)
                        },
                    )
                    if (state.selectionState.selectedIds.isNotEmpty()) {
                        DeleteMenuItem(
                            selectionState = state.selectionState,
                            onClick = {
                                eventSink(CollectionUiEvent.MarkSelectedItemsAsDeleted)
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        ids = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity?.toTab(),
                        ),
                    )
                },
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
            val entity = collection.entity
            val uiState = when (entity) {
                MusicBrainzEntity.AREA -> {
                    EntitiesPagingListUiState(
                        lazyPagingItems = areasLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.areasListUiState.lazyListState,
                    )
                }

                MusicBrainzEntity.ARTIST -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.artistsListUiState.lazyListState,
                        lazyPagingItems = artistsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.EVENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.eventsListUiState.lazyListState,
                        lazyPagingItems = eventsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.GENRE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.genresListUiState.lazyListState,
                        lazyPagingItems = genresLazyPagingItems,
                    )
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.instrumentsListUiState.lazyListState,
                        lazyPagingItems = instrumentsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.LABEL -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.labelsListUiState.lazyListState,
                        lazyPagingItems = labelsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.PLACE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.placesListUiState.lazyListState,
                        lazyPagingItems = placesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RECORDING -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.recordingsListUiState.lazyListState,
                        lazyPagingItems = recordingsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.RELEASE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.releasesListUiState.lazyListState,
                        lazyPagingItems = releasesLazyPagingItems,
                        showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                    )
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.releaseGroupsListUiState.lazyListState,
                        lazyPagingItems = releaseGroupsLazyPagingItems,
                    )
                }

                MusicBrainzEntity.SERIES -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.seriesListUiState.lazyListState,
                        lazyPagingItems = seriesLazyPagingItems,
                    )
                }

                MusicBrainzEntity.WORK -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.worksListUiState.lazyListState,
                        lazyPagingItems = worksLazyPagingItems,
                    )
                }

                else -> {
                    error("$entity is not supported for collections.")
                }
            }
            EntitiesPagingListUi(
                uiState = uiState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onItemClick = { entity, id, title ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entity = entity,
                            id = id,
                            title = title,
                        ),
                    )
                },
                selectedIds = state.selectionState.selectedIds,
                onSelect = {
                    state.selectionState.toggleSelection(
                        id = it,
                        totalLoadedCount = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity.toTab(),
                        ).size,
                    )
                },
                onEditCollectionClick = {
                    showAddToCollectionSheet(
                        coroutineScope = coroutineScope,
                        overlayHost = overlayHost,
                        entity = entity,
                        entityIds = setOf(it),
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(LoginUiEvent.StartLogin)
                        },
                    )
                },
                requestForMissingCoverArtUrl = { entityId ->
                    when (entity) {
                        MusicBrainzEntity.RELEASE -> {
                            releasesEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            releaseGroupsEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
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
