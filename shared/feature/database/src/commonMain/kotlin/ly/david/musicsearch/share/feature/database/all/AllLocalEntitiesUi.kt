package ly.david.musicsearch.share.feature.database.all

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ArtistSortOption
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.list.RecordingSortOption
import ly.david.musicsearch.shared.domain.list.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.list.ReleaseSortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.getListFilters
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.paging.toEntitiesPagingListUiState
import ly.david.musicsearch.ui.common.release.ShowStatusesMenuItem
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.sort.SortMenuItem
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllLocalEntitiesUi(
    state: AllLocalEntitiesUiState,
    modifier: Modifier = Modifier,
    now: Instant = Clock.System.now(),
) {
    val entity = state.entityType
    val eventSink = state.eventSink
    val recordingsByEntityEventSink = state.allEntitiesListUiState.recordingsListUiState.eventSink
    val releasesByEntityEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsByEntityEventSink = state.allEntitiesListUiState.releaseGroupsListUiState.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

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
        flowOf(PagingData.empty<ListItemModel>()).collectAsLazyPagingItems()
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

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AllLocalEntitiesUiEvent.NavigateUp)
                },
                title = stringResource(entity.getNamePlural()),
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    StatsMenuItem(
                        statsScreen = StatsScreen(
                            browseMethod = BrowseMethod.All,
                            tabs = listOfNotNull(entity.toTab()).toPersistentList(),
                            isRemote = false,
                        ),
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                    )
                    // TODO: generalize sort menu item
                    //  it's okay to have a SortMenuItem for an unused entity type in a screen
                    when (val listFilters = state.allEntitiesListUiState.getListFilters(entity)) {
                        is ListFilters.Base -> {
                            // nothing
                        }

                        is ListFilters.Artists -> {
                            SortMenuItem(
                                sortOptions = ArtistSortOption.entries,
                                selectedSortOption = listFilters.sortOption,
                                onSortOptionClick = {
                                    // TODO: only need one of these event sinks
                                    //  consider moving to AllEntitiesListUiEvent
                                    recordingsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortArtistListItem(it),
                                    )
                                },
                            )
                        }

                        is ListFilters.Recordings -> {
                            SortMenuItem(
                                sortOptions = RecordingSortOption.entries,
                                selectedSortOption = listFilters.sortOption,
                                onSortOptionClick = {
                                    recordingsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortRecordingListItem(it),
                                    )
                                },
                            )
                        }

                        is ListFilters.Releases -> {
                            ShowStatusesMenuItem(
                                selectedStatuses = listFilters.showStatuses,
                                onClick = {
                                    releasesByEntityEventSink(
                                        EntitiesListUiEvent.UpdateShowReleaseStatus(it),
                                    )
                                },
                            )
                            SortMenuItem(
                                sortOptions = ReleaseSortOption.entries,
                                selectedSortOption = listFilters.sortOption,
                                onSortOptionClick = {
                                    releasesByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortReleaseListItem(it),
                                    )
                                },
                            )
                            MoreInfoToggleMenuItem(
                                showMoreInfo = listFilters.showMoreInfo,
                                onToggle = {
                                    releasesByEntityEventSink(
                                        EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                    )
                                },
                            )
                        }

                        is ListFilters.ReleaseGroups -> {
                            SortMenuItem(
                                sortOptions = ReleaseGroupSortOption.entries,
                                selectedSortOption = listFilters.sortOption,
                                onSortOptionClick = {
                                    releaseGroupsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortReleaseGroupListItem(it),
                                    )
                                },
                            )
                        }

                        is ListFilters.Works -> {
                            // nothing
                        }
                    }
                    AddAllToCollectionMenuItem(
                        tab = entity.toTab(),
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        items = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity.toTab(),
                        ),
                    )
                },
            )
        },
    ) { innerPadding, scrollBehavior ->
        val uiState = state.allEntitiesListUiState.toEntitiesPagingListUiState(
            tab = entity.toTab(),
            entitiesLazyPagingItems = entitiesLazyPagingItems,
        )
        EntitiesPagingListUi(
            uiState = uiState,
            filterText = state.topAppBarFilterState.filterText,
            now = now,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onItemClick = { entity, id ->
                eventSink(
                    AllLocalEntitiesUiEvent.ClickItem(
                        entityType = entity,
                        id = id,
                    ),
                )
            },
            selectedIds = state.selectionState.selectedIds,
            onSelect = {
                state.selectionState.toggleSelection(
                    item = SelectableId(id = it),
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
                    entityIds = listOf(it),
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
