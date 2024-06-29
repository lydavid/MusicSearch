package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.artist.ArtistsListScreen
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.relation.RelationsListScreen
import ly.david.ui.common.screen.AddToCollectionScreen
import ly.david.ui.common.screen.StatsScreen
import ly.david.ui.common.screen.showInBottomSheet
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import ly.david.ui.core.LocalStrings

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
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

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(ReleaseUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(ReleaseUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                subtitle = state.subtitle,
                scrollBehavior = scrollBehavior,
                showFilterIcon = state.selectedTab !in listOf(
                    ReleaseTab.STATS,
                ),
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(ReleaseUiEvent.UpdateQuery(it))
                },
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        entity = entity,
                        entityId = entityId,
                    )
                    CopyToClipboardMenuItem(entityId)
                    AddToCollectionMenuItem {
                        scope.launch {
                            overlayHost.showInBottomSheet(
                                AddToCollectionScreen(
                                    entity = entity,
                                    id = entityId,
                                ),
                            )
                        }
                    }
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

        val detailsLazyListState = rememberLazyListState()
        val tracksLazyListState = rememberLazyListState()
        val artistsLazyListState = rememberLazyListState()
        val relationsLazyListState = rememberLazyListState()

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
                        showError = state.isError,
                        onRefresh = {
                            eventSink(ReleaseUiEvent.ForceRefresh)
                        },
                        scaffoldModel = state.release,
                    ) { release ->
                        ReleaseDetailsUi(
                            release = release,
                            filterText = state.query,
                            imageUrl = state.imageUrl,
                            lazyListState = detailsLazyListState,
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
                    TracksByReleaseScreen(
                        lazyListState = tracksLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = state.tracksByReleaseUiState.lazyPagingItems,
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
                        lazyPagingItems = state.relationsUiState.lazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = relationsLazyListState,
                        snackbarHostState = snackbarHostState,
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
