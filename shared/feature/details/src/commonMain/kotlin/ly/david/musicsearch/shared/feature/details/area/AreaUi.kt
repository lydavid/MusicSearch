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
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.artist.ArtistsListScreen
import ly.david.musicsearch.ui.common.event.EventsListScreen
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.label.LabelsListScreen
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.place.PlacesListScreen
import ly.david.musicsearch.ui.common.relation.RelationsListScreen
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListScreen
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

    val releasesByEntityEventSink = state.releasesByEntityUiState.eventSink
    val loginEventSink = state.loginUiState.eventSink

    AreaUiInternal(
        state = state,
        entityId = entityId,
        modifier = modifier,
        overflowDropdownMenuItems = {
            OpenInBrowserMenuItem(
                url = state.url,
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
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                        showError = state.isError,
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
                    ArtistsListScreen(
                        lazyListState = state.artistsByEntityUiState.lazyListState,
                        lazyPagingItems = state.artistsByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        lazyListState = state.eventsByEntityUiState.lazyListState,
                        lazyPagingItems = state.eventsByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
                    LabelsListScreen(
                        lazyPagingItems = state.labelsByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = state.labelsByEntityUiState.lazyListState,
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
                    ReleasesListScreen(
                        lazyPagingItems = state.releasesByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = state.releasesByEntityUiState.lazyListState,
                        showMoreInfo = state.releasesByEntityUiState.showMoreInfo,
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
                    PlacesListScreen(
                        lazyListState = state.placesByEntityUiState.lazyListState,
                        lazyPagingItems = state.placesByEntityUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
