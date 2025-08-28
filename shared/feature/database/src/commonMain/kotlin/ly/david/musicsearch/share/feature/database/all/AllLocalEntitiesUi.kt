package ly.david.musicsearch.share.feature.database.all

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.SortToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllLocalEntitiesUi(
    state: AllLocalEntitiesUiState,
    modifier: Modifier = Modifier,
) {
    val entity = state.entity
    val eventSink = state.eventSink
    val releasesEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.allEntitiesListUiState.releaseGroupsListUiState.eventSink
    val loginEventSink = state.loginUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AllLocalEntitiesUiEvent.NavigateUp)
                },
                title = entity.getNamePlural(strings),
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
                    if (entity == MusicBrainzEntityType.RELEASE_GROUP) {
                        SortToggleMenuItem(
                            sorted = state.allEntitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(EntitiesListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (entity == MusicBrainzEntityType.RELEASE) {
                        MoreInfoToggleMenuItem(
                            showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                    AddAllToCollectionMenuItem(
                        tab = entity.toTab(),
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(LoginUiEvent.StartLogin)
                        },
                    )
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        ids = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity.toTab(),
                        ),
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
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
                    showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                )
            }

            MusicBrainzEntityType.RELEASE_GROUP -> {
                EntitiesPagingListUiState(
                    lazyListState = state.allEntitiesListUiState.releaseGroupsListUiState.lazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems,
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
                error("$entity is not supported.")
            }
        }
        EntitiesPagingListUi(
            uiState = uiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onItemClick = { entity, id ->
                eventSink(
                    AllLocalEntitiesUiEvent.ClickItem(
                        entity = entity,
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
                    MusicBrainzEntityType.RELEASE -> {
                        releasesEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                    }

                    MusicBrainzEntityType.RELEASE_GROUP -> {
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
