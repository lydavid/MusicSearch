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
    val entity = MusicBrainzEntity.AREA

    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val overlayHost = LocalOverlayHost.current

    val releasesByEntityEventSink = state.releasesListUiState.eventSink
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
        overflowDropdownMenuItems = {
            OpenInBrowserMenuItem(
                url = state.url,
            )
            CopyToClipboardMenuItem(entityId)
            if (state.selectedTab == AreaTab.RELEASES) {
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
            AddToCollectionMenuItem(
                entity = entity,
                entityId = entityId,
                overlayHost = overlayHost,
                coroutineScope = scope,
                snackbarHostState = snackbarHostState,
                onLoginClick = {
                    loginEventSink(LoginUiEvent.StartLogin)
                },
            )
        },
        strings = strings,
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
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    strings: AppStrings = LocalStrings.current,
    now: Instant = Clock.System.now(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val entity = MusicBrainzEntity.AREA
    val eventSink = state.eventSink
    val releasesByEntityEventSink = state.releasesListUiState.eventSink
    val pagerState = rememberPagerState(
        initialPage = state.tabs.indexOf(state.selectedTab),
        pageCount = state.tabs::size,
    )

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
                overflowDropdownMenuItems = overflowDropdownMenuItems,
                topAppBarFilterState = state.topAppBarFilterState,
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
                        handledException = state.handledException,
                        onRefresh = {
                            eventSink(AreaUiEvent.ForceRefresh)
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

                AreaTab.ARTISTS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
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

                AreaTab.EVENTS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
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

                AreaTab.LABELS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.labelsListUiState.lazyPagingItems,
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

                AreaTab.RELEASES -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.releasesListUiState.lazyPagingItems,
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

                AreaTab.RELATIONSHIPS -> {
                    RelationsListScreen(
                        lazyPagingItems = state.relationsUiState.lazyPagingItems,
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

                AreaTab.PLACES -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.placesListUiState.lazyPagingItems,
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
