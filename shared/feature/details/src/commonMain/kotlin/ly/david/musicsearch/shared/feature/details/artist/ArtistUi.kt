package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
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
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.list.EntitiesListScreen
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsListScreen
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun ArtistUi(
    state: DetailsUiState<ArtistDetailsModel>,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val overlayHost = LocalOverlayHost.current
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val entity = MusicBrainzEntity.ARTIST
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(pageCount = state.tabs::size)

    val eventsLazyPagingItems =
        state.entitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.entitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesByEntityEventSink =
        state.entitiesListUiState.releasesListUiState.eventSink
    val releaseGroupLazyPagingItems =
        state.entitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsByEntityEventSink =
        state.entitiesListUiState.releaseGroupsListUiState.eventSink
    val worksLazyPagingItems =
        state.entitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.entitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val relationsLazyPagingItems =
        state.relationsUiState.pagingDataFlow.collectAsLazyPagingItems()

    val loginEventSink = state.loginUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(DetailsUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
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
                    eventSink(DetailsUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    val selectedTab = state.selectedTab

                    RefreshMenuItem(
                        show = selectedTab != Tab.STATS,
                        onClick = {
                            when (selectedTab) {
                                Tab.RELEASE_GROUPS -> releaseGroupLazyPagingItems.refresh()
                                Tab.RELEASES -> releasesLazyPagingItems.refresh()
                                Tab.RECORDINGS -> recordingsLazyPagingItems.refresh()
                                Tab.WORKS -> worksLazyPagingItems.refresh()
                                Tab.EVENTS -> eventsLazyPagingItems.refresh()
                                Tab.RELATIONSHIPS -> relationsLazyPagingItems.refresh()
                                else -> eventSink(DetailsUiEvent.ForceRefreshDetails)
                            }
                        },
                    )
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (selectedTab == Tab.RELEASE_GROUPS) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.entitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsByEntityEventSink(
                                    ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it),
                                )
                            },
                        )
                    }
                    if (selectedTab == Tab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesByEntityEventSink(
                                    ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                )
                            },
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(strings.seeCollaborators) },
                        onClick = {
                            eventSink(DetailsUiEvent.NavigateToCollaboratorsGraph)
                            closeMenu()
                        },
                    )
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
                        isLoading = state.detailsTabUiState.isLoading,
                        handledException = state.detailsTabUiState.handledException,
                        onRefresh = {
                            eventSink(DetailsUiEvent.ForceRefreshDetails)
                        },
                        detailsModel = state.detailsModel,
                    ) { artist ->
                        ArtistDetailsUi(
                            artist = artist,
                            detailsTabUiState = state.detailsTabUiState,
                            filterText = state.topAppBarFilterState.filterText,
                            onItemClick = { entity, id, title ->
                                eventSink(
                                    DetailsUiEvent.ClickItem(
                                        entity = entity,
                                        id = id,
                                        title = title,
                                    ),
                                )
                            },
                            onCollapseExpandExternalLinks = {
                                eventSink(DetailsUiEvent.ToggleCollapseExpandExternalLinks)
                            },
                        )
                    }
                }

                Tab.RELEASE_GROUPS -> {
                    EntitiesListScreen(
                        uiState = EntitiesPagingListUiState(
                            lazyPagingItems = releaseGroupLazyPagingItems,
                            lazyListState = state.entitiesListUiState.releaseGroupsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                DetailsUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                        requestForMissingCoverArtUrl = { id ->
                            releaseGroupsByEntityEventSink(
                                ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl(
                                    entityId = id,
                                ),
                            )
                        },
                    )
                }

                Tab.RELEASES -> {
                    EntitiesListScreen(
                        uiState = EntitiesPagingListUiState(
                            lazyPagingItems = releasesLazyPagingItems,
                            lazyListState = state.entitiesListUiState.releasesListUiState.lazyListState,
                            showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                DetailsUiEvent.ClickItem(
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

                Tab.RECORDINGS -> {
                    EntitiesListScreen(
                        uiState = EntitiesPagingListUiState(
                            lazyPagingItems = recordingsLazyPagingItems,
                            lazyListState = state.entitiesListUiState.recordingsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                DetailsUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                Tab.WORKS -> {
                    EntitiesListScreen(
                        uiState = EntitiesPagingListUiState(
                            lazyPagingItems = worksLazyPagingItems,
                            lazyListState = state.entitiesListUiState.worksListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                DetailsUiEvent.ClickItem(
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
                        uiState = EntitiesPagingListUiState(
                            lazyPagingItems = eventsLazyPagingItems,
                            lazyListState = state.entitiesListUiState.eventsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                DetailsUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
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
                                DetailsUiEvent.ClickItem(
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
