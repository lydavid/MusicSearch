package ly.david.musicsearch.shared.feature.details.release

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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.list.EntitiesListScreen
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsListScreen
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun ReleaseUi(
    state: ReleaseUiState,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val overlayHost = LocalOverlayHost.current
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val entity = MusicBrainzEntity.RELEASE
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(pageCount = state.tabs::size)

    val relationsLazyPagingItems = state.relationsUiState.pagingDataFlow.collectAsLazyPagingItems()

    val loginEventSink = state.loginUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(ReleaseUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
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
                    eventSink(ReleaseUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                subtitle = state.subtitle,
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                    CopyToClipboardMenuItem(entityId)
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
                subtitleDropdownMenuItems = {
                    state.release?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.ARTIST) },
                            onClick = {
                                closeMenu()
                                eventSink(
                                    ReleaseUiEvent.ClickItem(
                                        entity = MusicBrainzEntity.ARTIST,
                                        id = artistCredit.artistId,
                                        title = null,
                                    ),
                                )
                            },
                        )
                    }
                    state.release?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(
                            text = { Text(text = releaseGroup.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                eventSink(
                                    ReleaseUiEvent.ClickItem(
                                        entity = MusicBrainzEntity.RELEASE_GROUP,
                                        id = releaseGroup.id,
                                        title = null,
                                    ),
                                )
                            },
                        )
                    }
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

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (state.tabs[page]) {
                ReleaseTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        handledException = state.releaseDetailsUiState.handledException,
                        onRefresh = {
                            eventSink(ReleaseUiEvent.ForceRefresh)
                        },
                        detailsModel = state.release,
                    ) { release ->
                        ReleaseDetailsUi(
                            release = release,
                            releaseDetailsUiState = state.releaseDetailsUiState,
                            filterText = state.topAppBarFilterState.filterText,
                            onImageClick = {
                                eventSink(ReleaseUiEvent.ClickImage)
                            },
                            onCollapseExpand = {
                                eventSink(ReleaseUiEvent.ToggleCollapseExpandReleaseEvents)
                            },
                            onItemClick = { entity, id, title ->
                                eventSink(
                                    ReleaseUiEvent.ClickItem(
                                        entity = entity,
                                        id = id,
                                        title = title,
                                    ),
                                )
                            },
                        )
                    }
                }

                ReleaseTab.TRACKS -> {
                    TracksByReleaseUi(
                        uiState = state.tracksByReleaseUiState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onRecordingClick = { id, title ->
                            eventSink(
                                ReleaseUiEvent.ClickItem(
                                    entity = MusicBrainzEntity.RECORDING,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                ReleaseTab.ARTISTS -> {
                    EntitiesListScreen(
                        uiState = EntitiesListUiState(
                            lazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                            lazyListState = state.artistsListUiState.lazyListState,
                        ),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onItemClick = { entity, id, title ->
                            eventSink(
                                ReleaseUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                ReleaseTab.RELATIONSHIPS -> {
                    RelationsListScreen(
                        lazyPagingItems = relationsLazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = state.relationsUiState.lazyListState,
                        onItemClick = { entity, id, title ->
                            eventSink(
                                ReleaseUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                ReleaseTab.STATS -> {
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
