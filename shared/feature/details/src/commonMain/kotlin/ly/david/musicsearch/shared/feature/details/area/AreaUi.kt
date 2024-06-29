package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.artist.ArtistsListScreen
import ly.david.ui.common.event.EventsListScreen
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.label.LabelsListScreen
import ly.david.ui.common.place.PlacesListScreen
import ly.david.ui.common.relation.RelationsListScreen
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.release.ReleasesListScreen
import ly.david.ui.common.screen.AddToCollectionScreen
import ly.david.ui.common.screen.StatsScreen
import ly.david.ui.common.screen.showInBottomSheet
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import ly.david.ui.core.LocalStrings

/**
 * The top-level screen for an area.
 */
@Composable
internal fun AreaUi(
    state: AreaUiState,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val overlayHost = LocalOverlayHost.current
    val scope = rememberCoroutineScope()

    AreaUi(
        state = state,
        entityId = entityId,
        modifier = modifier,
        scope = scope,
        onAddToCollectionClick = {
            scope.launch {
                overlayHost.showInBottomSheet(
                    AddToCollectionScreen(
                        entity = MusicBrainzEntity.AREA,
                        id = entityId,
                    ),
                )
            }
        },
    )
}

/**
 * The top-level screen for an area.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun AreaUi(
    state: AreaUiState,
    entityId: String,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onAddToCollectionClick: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val entity = MusicBrainzEntity.AREA
    val eventSink = state.eventSink
    val releasesByEntityEventSink = state.releasesByEntityUiState.eventSink
    val pagerState = rememberPagerState(
        initialPage = state.tabs.indexOf(state.selectedTab),
        pageCount = state.tabs::size,
    )

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(AreaUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AreaUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        entity = MusicBrainzEntity.AREA,
                        entityId = entityId,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (state.selectedTab == AreaTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesByEntityUiState.showMoreInfo,
                            onToggle = {
                                releasesByEntityEventSink(
                                    ReleasesByEntityUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                )
                            },
                        )
                    }
                    AddToCollectionMenuItem(
                        onClick = onAddToCollectionClick,
                    )
                },
                showFilterIcon = state.selectedTab !in listOf(
                    AreaTab.STATS,
                ),
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(AreaUiEvent.UpdateQuery(it))
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = state.tabs.map { it.tab.getTitle(strings) },
                        selectedTabIndex = state.tabs.indexOf(state.selectedTab),
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()
        val relationsLazyListState = rememberLazyListState()
        val artistsLazyListState = rememberLazyListState()
        val eventsLazyListState = rememberLazyListState()
        val labelsLazyListState = rememberLazyListState()
        val releasesLazyListState = rememberLazyListState()
        val placesLazyListState = rememberLazyListState()

        // TODO: consider generalizing this entire details ui, we can have all types of tabs here
        //  each details screen will provide its own list of tabs (which may be in different order)
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (state.tabs[page]) {
                AreaTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        showError = state.isError,
                        onRefresh = {
                            eventSink(AreaUiEvent.ForceRefresh)
                        },
                        scaffoldModel = state.area,
                    ) {
                        AreaDetailsUi(
                            area = it,
                            filterText = state.query,
                            lazyListState = detailsLazyListState,
                            onItemClick = { entity, id, title ->
                                eventSink(
                                    AreaUiEvent.ClickItem(
                                        entity = entity,
                                        id = id,
                                        title = title,
                                    ),
                                )
                            },
                        )
                    }
                }

                AreaTab.ARTISTS -> {
                    ArtistsListScreen(
                        lazyListState = artistsLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = state.artistsByEntityUiState.lazyPagingItems,
                        onItemClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                AreaTab.EVENTS -> {
                    EventsListScreen(
                        lazyListState = labelsLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = state.eventsByEntityUiState.lazyPagingItems,
                        onEventClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                AreaTab.LABELS -> {
                    LabelsListScreen(
                        lazyListState = eventsLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = state.labelsByEntityUiState.lazyPagingItems,
                        onLabelClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                AreaTab.RELEASES -> {
                    ReleasesListScreen(
                        lazyListState = releasesLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = state.releasesByEntityUiState.lazyPagingItems,
                        showMoreInfo = state.releasesByEntityUiState.showMoreInfo,
                        onReleaseClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                        requestForMissingCoverArtUrl = { id ->
                            releasesByEntityEventSink(
                                ReleasesByEntityUiEvent.RequestForMissingCoverArtUrl(
                                    entityId = id,
                                ),
                            )
                        },
                    )
                }

                AreaTab.RELATIONSHIPS -> {
                    RelationsListScreen(
                        lazyPagingItems = state.relationsUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = relationsLazyListState,
                        snackbarHostState = snackbarHostState,
                        onItemClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                AreaTab.PLACES -> {
                    PlacesListScreen(
                        snackbarHostState = snackbarHostState,
                        lazyListState = placesLazyListState,
                        lazyPagingItems = state.placesByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onPlaceClick = { entity, id, title ->
                            eventSink(
                                AreaUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                AreaTab.STATS -> {
                    CircuitContent(
                        StatsScreen(
                            entity = entity,
                            id = entityId,
                            tabs = state.tabs.map { it.tab },
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                    )
                }
            }
        }
    }
}
