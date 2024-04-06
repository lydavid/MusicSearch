package ly.david.musicsearch.shared.feature.details.releasegroup

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
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
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

/**
 * Equivalent to a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Starts on a screen that displays all of its releases.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun ReleaseGroupUi(
    state: ReleaseGroupUiState,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val overlayHost = LocalOverlayHost.current
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val entity = MusicBrainzEntity.RELEASE_GROUP
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(pageCount = state.tabs::size)

    val releasesByEntityEventSink = state.releasesByEntityUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(ReleaseGroupUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(ReleaseGroupUiEvent.NavigateUp)
                },
                entity = entity,
                title = state.title,
                subtitle = state.subtitle,
                scrollBehavior = scrollBehavior,
                showFilterIcon = state.selectedTab !in listOf(
                    ReleaseGroupTab.STATS,
                ),
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        entity = entity,
                        entityId = entityId,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (state.selectedTab == ReleaseGroupTab.RELEASES) {
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
                    state.releaseGroup?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.ARTIST) },
                            onClick = {
                                closeMenu()
                                // Don't pass a title, because the name used here may not be the name used for the
                                // the artist's page.
                                eventSink(
                                    ReleaseGroupUiEvent.ClickItem(
                                        entity = MusicBrainzEntity.ARTIST,
                                        id = artistCredit.artistId,
                                        title = null,
                                    ),
                                )
                            },
                        )
                    }
                },
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(ReleaseGroupUiEvent.UpdateQuery(it))
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
        val releasesLazyListState = rememberLazyListState()
        val relationsLazyListState = rememberLazyListState()

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (state.tabs[page]) {
                ReleaseGroupTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = state.isError,
                        onRetryClick = {
                            eventSink(ReleaseGroupUiEvent.ForceRefresh)
                        },
                        scaffoldModel = state.releaseGroup,
                    ) { releaseGroup ->
                        ReleaseGroupDetailsUi(
                            releaseGroup = releaseGroup,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = state.query,
                            imageUrl = state.imageUrl,
                            lazyListState = detailsLazyListState,
                            onItemClick = { entity, id, title ->
                                eventSink(
                                    ReleaseGroupUiEvent.ClickItem(
                                        entity = entity,
                                        id = id,
                                        title = title,
                                    ),
                                )
                            },
                        )
                    }
                }

                ReleaseGroupTab.RELEASES -> {
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
                                ReleaseGroupUiEvent.ClickItem(
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

                ReleaseGroupTab.RELATIONSHIPS -> {
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
                                ReleaseGroupUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                    title = title,
                                ),
                            )
                        },
                    )
                }

                ReleaseGroupTab.STATS -> {
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
