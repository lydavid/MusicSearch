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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.SortOption
import ly.david.musicsearch.shared.domain.list.showTypes
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSortOption
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.getSortOption
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.getLazyPagingItemsForTab
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.sort.SortMenuItem
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.DeleteMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Instant

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
    now: Instant = Clock.System.now(),
) {
    val collection = state.collection
    val entity = collection?.entity
    val eventSink = state.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink
    val recordingsByEntityEventSink = state.allEntitiesListUiState.recordingsListUiState.eventSink
    val releasesByEntityEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsByEntityEventSink = state.allEntitiesListUiState.releaseGroupsListUiState.eventSink
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
                        eventSink(CollectionUiEvent.DeleteItemsMarkedAsDeleted)
                    }
                }
            } catch (_: CancellationException) {
                eventSink(CollectionUiEvent.DeleteItemsMarkedAsDeleted)
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
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
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
                            browseMethod = BrowseMethod.ByEntity(
                                entityId = collection?.id.orEmpty(),
                                entityType = MusicBrainzEntityType.COLLECTION,
                            ),
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
                    when (val sortOption = state.allEntitiesListUiState.getSortOption(entity)) {
                        SortOption.None -> {
                            // nothing
                        }

                        is SortOption.Recording -> {
                            SortMenuItem(
                                sortOptions = RecordingSortOption.entries,
                                selectedSortOption = sortOption.option,
                                onSortOptionClick = {
                                    recordingsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortRecordingListItem(it),
                                    )
                                },
                            )
                        }

                        is SortOption.Release -> {
                            SortMenuItem(
                                sortOptions = ReleaseSortOption.entries,
                                selectedSortOption = sortOption.option,
                                onSortOptionClick = {
                                    releasesByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortReleaseListItem(it),
                                    )
                                },
                            )
                            MoreInfoToggleMenuItem(
                                showMoreInfo = sortOption.showMoreInfo,
                                onToggle = {
                                    releasesByEntityEventSink(
                                        EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                    )
                                },
                            )
                        }

                        is SortOption.ReleaseGroup -> {
                            SortMenuItem(
                                sortOptions = ReleaseGroupSortOption.entries,
                                selectedSortOption = sortOption.option,
                                onSortOptionClick = {
                                    releaseGroupsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortReleaseGroupListItem(it),
                                    )
                                },
                            )
                        }
                    }
                    AddAllToCollectionMenuItem(
                        tab = entity?.toTab(),
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
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
                MusicBrainzEntityType.AREA -> {
                    EntitiesPagingListUiState(
                        lazyPagingItems = areasLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.areasListUiState.lazyListState,
                    )
                }

                MusicBrainzEntityType.ARTIST -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.artistsListUiState.lazyListState,
                        lazyPagingItems = artistsLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.EVENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.eventsListUiState.lazyListState,
                        lazyPagingItems = eventsLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.GENRE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.genresListUiState.lazyListState,
                        lazyPagingItems = genresLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.INSTRUMENT -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.instrumentsListUiState.lazyListState,
                        lazyPagingItems = instrumentsLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.LABEL -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.labelsListUiState.lazyListState,
                        lazyPagingItems = labelsLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.PLACE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.placesListUiState.lazyListState,
                        lazyPagingItems = placesLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.RECORDING -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.recordingsListUiState.lazyListState,
                        lazyPagingItems = recordingsLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.RELEASE -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.releasesListUiState.lazyListState,
                        lazyPagingItems = releasesLazyPagingItems,
                        showMoreInfo = (
                            state.allEntitiesListUiState.releasesListUiState.sortOption as? SortOption.Release
                            )?.showMoreInfo == true,
                    )
                }

                MusicBrainzEntityType.RELEASE_GROUP -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.releaseGroupsListUiState.lazyListState,
                        lazyPagingItems = releaseGroupsLazyPagingItems,
                        showMoreInfo = state.allEntitiesListUiState.releaseGroupsListUiState.sortOption.showTypes(),
                    )
                }

                MusicBrainzEntityType.SERIES -> {
                    EntitiesPagingListUiState(
                        lazyListState = state.allEntitiesListUiState.seriesListUiState.lazyListState,
                        lazyPagingItems = seriesLazyPagingItems,
                    )
                }

                MusicBrainzEntityType.WORK -> {
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
                now = now,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onItemClick = { entity, id ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entityType = entity,
                            id = id,
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
                        entityType = entity,
                        entityIds = setOf(it),
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                },
                requestForMissingCoverArtUrl = { entityId ->
                    when (entity) {
                        MusicBrainzEntityType.RELEASE -> {
                            releasesByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        MusicBrainzEntityType.RELEASE_GROUP -> {
                            releaseGroupsByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
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
