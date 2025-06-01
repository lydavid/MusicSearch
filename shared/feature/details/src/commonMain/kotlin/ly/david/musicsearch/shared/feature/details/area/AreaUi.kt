package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.list.EntitiesListScreen
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsListScreen
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun AreaUi(
    state: AreaUiState,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val overlayHost = LocalOverlayHost.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val loginEventSink = state.loginUiState.eventSink

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    AreaUiInternal(
        state = state,
        entityId = entityId,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        strings = strings,
        additionalOverflowDropdownMenuItems = {
            AddToCollectionMenuItem(
                entity = MusicBrainzEntity.AREA,
                entityId = entityId,
                overlayHost = overlayHost,
                coroutineScope = scope,
                snackbarHostState = snackbarHostState,
                onLoginClick = {
                    loginEventSink(LoginUiEvent.StartLogin)
                },
            )
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun AreaUiInternal(
    state: AreaUiState,
    entityId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    strings: AppStrings = LocalStrings.current,
    now: Instant = Clock.System.now(),
    additionalOverflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit) = {},
) {
    val entity = MusicBrainzEntity.AREA
    val pagerState = rememberPagerState(
        initialPage = state.tabs.indexOf(state.selectedTab),
        pageCount = state.tabs::size,
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val eventSink = state.eventSink
    val relationsLazyPagingItems = state.relationsUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems = state.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems = state.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems = state.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesByEntityEventSink = state.releasesListUiState.eventSink
    val placesLazyPagingItems = state.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(AreaUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AreaUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    RefreshMenuItem(
                        show = state.selectedTab != Tab.STATS,
                        onClick = {
                            when (state.selectedTab) {
                                Tab.ARTISTS -> artistsLazyPagingItems.refresh()
                                Tab.EVENTS -> eventsLazyPagingItems.refresh()
                                Tab.LABELS -> labelsLazyPagingItems.refresh()
                                Tab.PLACES -> placesLazyPagingItems.refresh()
                                Tab.RELATIONSHIPS -> relationsLazyPagingItems.refresh()
                                Tab.RELEASES -> releasesLazyPagingItems.refresh()
                                else -> eventSink(AreaUiEvent.ForceRefreshDetails)
                            }
                        },
                    )
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (state.selectedTab == Tab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesByEntityEventSink(
                                    ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                )
                            },
                        )
                    }

                    additionalOverflowDropdownMenuItems()
                },
                topAppBarFilterState = state.topAppBarFilterState,
                additionalBar = {
                    TabsBar(
                        tabsTitle = state.tabs.map { it.getTitle(strings) },
                        selectedTabIndex = state.tabs.indexOf(state.selectedTab),
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
    ) { innerPadding ->

        // TODO: consider generalizing this entire details ui, we can have all types of tabs here
        //  each details screen will provide its own list of tabs (which may be in different order)
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (state.tabs[page]) {
                Tab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        handledException = state.handledException,
                        onRefresh = {
                            eventSink(AreaUiEvent.ForceRefreshDetails)
                        },
                        detailsModel = state.area,
                    ) {
                        AreaDetailsUi(
                            area = it,
                            filterText = state.topAppBarFilterState.filterText,
                            lazyListState = state.detailsLazyListState,
                        )
                    }
                }

                Tab.RELATIONSHIPS -> {
                    RelationsListScreen(
                        lazyPagingItems = relationsLazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = state.relationsUiState.lazyListState,
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

                Tab.ARTISTS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = artistsLazyPagingItems,
                            lazyListState = state.artistsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        now = now,
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

                Tab.EVENTS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = eventsLazyPagingItems,
                            lazyListState = state.eventsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        now = now,
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

                Tab.LABELS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = labelsLazyPagingItems,
                            lazyListState = state.labelsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        now = now,
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

                Tab.RELEASES -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = releasesLazyPagingItems,
                            lazyListState = state.releasesListUiState.lazyListState,
                            showMoreInfo = state.releasesListUiState.showMoreInfo,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),

                        now = now,
                        onItemClick = { entity, id, title ->
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
                                ReleasesListUiEvent.RequestForMissingCoverArtUrl(
                                    entityId = id,
                                ),
                            )
                        },
                    )
                }

                Tab.PLACES -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = placesLazyPagingItems,
                            lazyListState = state.placesListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        now = now,
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

                Tab.STATS -> {
                    CircuitContent(
                        StatsScreen(
                            entity = entity,
                            id = entityId,
                            tabs = state.tabs,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                    )
                }

                else -> {
                    // no-op
                }
            }
        }
    }
}
