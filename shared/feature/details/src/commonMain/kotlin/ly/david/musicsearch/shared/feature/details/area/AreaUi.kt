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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle

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
//    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
//    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
) {
    val eventSink = state.eventSink
    val resource = MusicBrainzEntity.AREA
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.DETAILS) }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = state.tabs::size)

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = state.tabs[pagerState.currentPage]
    }

//    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
//        viewModel.loadDataForTab(
//            areaId = areaId,
//            selectedTab = selectedTab,
//        )
//    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AreaUiEvent.NavigateUp)
                },
                entity = resource,
                title = state.title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        entity = MusicBrainzEntity.AREA,
                        entityId = entityId,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (selectedTab == AreaTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.showMoreInfoInReleaseListItem,
                            onToggle = {
                                eventSink(AreaUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                    AddToCollectionMenuItem {
//                        onAddToCollectionMenuClick(resource, areaId)
                    }
                },
                showFilterIcon = selectedTab !in listOf(
                    AreaTab.STATS,
                ),
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(AreaUiEvent.UpdateQuery(it))
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = state.tabs.map { it.tab.getTitle(strings) },
                        selectedTabIndex = state.tabs.indexOf(selectedTab),
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

//        val relationsLazyListState = rememberLazyListState()
//        val relationsLazyPagingItems =
//            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
//                .collectAsLazyPagingItems()

//        val releasesLazyListState = rememberLazyListState()
//        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
//        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
//            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
//                .collectAsLazyPagingItems()
//
//        val placesLazyListState = rememberLazyListState()
//        var pagedPlacesFlow: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
//        val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel> =
//            rememberFlowWithLifecycleStarted(pagedPlacesFlow)
//                .collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (state.tabs[page]) {
                AreaTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        showError = state.isError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = state.area,
                    ) {
                        AreaDetailsScreen(
                            area = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = state.query,
                            lazyListState = detailsLazyListState,
//                            onItemClick = onItemClick,
                        )
                    }
                }

                AreaTab.RELATIONSHIPS -> {
                    Text("Rel")
//                    viewModel.updateQuery(filterText)
//                    RelationsListScreen(
//                        lazyPagingItems = relationsLazyPagingItems,
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .fillMaxSize()
//                            .nestedScroll(scrollBehavior.nestedScrollConnection),
//                        lazyListState = relationsLazyListState,
//                        snackbarHostState = snackbarHostState,
//                        onItemClick = onItemClick,
//                    )
                }

                AreaTab.RELEASES -> {
                    Text("Releases")
//                    ReleasesByAreaScreen(
//                        areaId = areaId,
//                        filterText = filterText,
//                        showMoreInfo = showMoreInfoInReleaseListItem,
//                        snackbarHostState = snackbarHostState,
//                        releasesLazyListState = releasesLazyListState,
//                        releasesLazyPagingItems = releasesLazyPagingItems,
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .fillMaxSize()
//                            .nestedScroll(scrollBehavior.nestedScrollConnection),
//                        onReleaseClick = onItemClick,
//                        onPagedReleasesFlowChange = { pagedReleasesFlow = it },
//                    )
                }

                AreaTab.PLACES -> {
                    Text("places")
//                    PlacesByAreaScreen(
//                        areaId = areaId,
//                        filterText = filterText,
//                        placesLazyListState = placesLazyListState,
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .fillMaxSize()
//                            .nestedScroll(scrollBehavior.nestedScrollConnection),
//                        snackbarHostState = snackbarHostState,
//                        placesLazyPagingItems = placesLazyPagingItems,
//                        onPagedPlacesFlowChange = { pagedPlacesFlow = it },
//                        onPlaceClick = onItemClick,
//                    )
                }

                AreaTab.STATS -> {
                    Text("stats")
//                    AreaStatsScreen(
//                        areaId = areaId,
//                        tabs = areaTabs.map { it.tab }.toImmutableList(),
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .fillMaxSize()
//                            .nestedScroll(scrollBehavior.nestedScrollConnection),
//                    )
                }
            }
        }
    }
}
