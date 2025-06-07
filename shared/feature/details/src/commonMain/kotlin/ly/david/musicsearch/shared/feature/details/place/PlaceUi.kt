package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.foundation.layout.WindowInsets
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
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.utils.DetailsHorizontalPager
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.common.theme.LocalStrings

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun PlaceUi(
    state: DetailsUiState<PlaceDetailsModel>,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val overlayHost = LocalOverlayHost.current
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val entity = MusicBrainzEntity.PLACE
    val browseMethod = BrowseMethod.ByEntity(entityId, entity)
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(pageCount = state.tabs::size)

    val areasLazyPagingItems =
        state.entitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems =
        state.entitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems =
        state.entitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems =
        state.entitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems =
        state.entitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems =
        state.entitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems =
        state.entitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.entitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.entitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems =
        state.entitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems =
        state.entitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems =
        state.entitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val relationsLazyPagingItems =
        state.entitiesListUiState.relationsUiState.pagingDataFlow.collectAsLazyPagingItems()
    val tracksLazyPagingItems =
        state.entitiesListUiState.tracksByReleaseUiState.pagingDataFlow.collectAsLazyPagingItems()
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
        relationsLazyPagingItems = relationsLazyPagingItems,
        tracksLazyPagingItems = tracksLazyPagingItems,
    )

    val loginEventSink = state.loginUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(DetailsUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
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
                        tab = selectedTab,
                        onClick = {
                            when (selectedTab) {
                                Tab.RELATIONSHIPS -> relationsLazyPagingItems.refresh()
                                Tab.EVENTS -> eventsLazyPagingItems.refresh()
                                else -> eventSink(DetailsUiEvent.ForceRefreshDetails)
                            }
                        },
                    )
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

        DetailsHorizontalPager(
            pagerState = pagerState,
            state = state,
            innerPadding = innerPadding,
            scrollBehavior = scrollBehavior,
            browseMethod = browseMethod,
            entityLazyPagingItems = entitiesLazyPagingItems,
            detailsScreen = { detailsModel ->
                PlaceDetailsTabUi(
                    place = detailsModel,
                    filterText = state.topAppBarFilterState.filterText,
                    detailsTabUiState = state.detailsTabUiState,
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
            },
        )
    }
}
