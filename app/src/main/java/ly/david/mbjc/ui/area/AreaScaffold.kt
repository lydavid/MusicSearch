package ly.david.mbjc.ui.area

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.details.AreaDetailsScreen
import ly.david.mbjc.ui.area.places.PlacesByAreaScreen
import ly.david.mbjc.ui.area.releases.ReleasesByAreaScreen
import ly.david.mbjc.ui.area.stats.AreaStatsScreen
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.relation.RelationsScreen
import ly.david.ui.common.R
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter

/**
 * The top-level screen for an area.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun AreaScaffold(
    areaId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    viewModel: AreaScaffoldViewModel = hiltViewModel(),
) {
    val resource = MusicBrainzResource.AREA
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val title by viewModel.title.collectAsState()
    val area by viewModel.area.collectAsState()
    val areaTabs by viewModel.areaTabs.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = areaId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = areaTabs[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            areaId = areaId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = onBack,
                resource = resource,
                title = title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.AREA, resourceId = areaId)
                    CopyToClipboardMenuItem(areaId)
                    if (selectedTab == AreaTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = R.string.show_more_info,
                            toggleOffText = R.string.show_less_info,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange
                        )
                    }
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, areaId)
                    }
                },
                tabsTitles = areaTabs.map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = areaTabs.indexOf(selectedTab),
                onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                showFilterIcon = selectedTab in listOf(
                    AreaTab.RELATIONSHIPS,
                    AreaTab.RELEASES,
                    AreaTab.PLACES
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val placesLazyListState = rememberLazyListState()
        var pagedPlacesFlow: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel> =
            rememberFlowWithLifecycleStarted(pagedPlacesFlow)
                .collectAsLazyPagingItems()

        HorizontalPager(
            pageCount = areaTabs.size,
            state = pagerState
        ) { page ->
            when (areaTabs[page]) {
                AreaTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = area
                    ) {
                        AreaDetailsScreen(
                            area = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            lazyListState = detailsLazyListState
                        )
                    }
                }

                AreaTab.RELATIONSHIPS -> {
                    viewModel.updateQuery(filterText)
                    RelationsScreen(
                        lazyPagingItems = relationsLazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        lazyListState = relationsLazyListState,
                        snackbarHostState = snackbarHostState,
                        onItemClick = onItemClick,
                    )
                }

                AreaTab.RELEASES -> {
                    ReleasesByAreaScreen(
                        areaId = areaId,
                        filterText = filterText,
                        showMoreInfo = showMoreInfoInReleaseListItem,
                        snackbarHostState = snackbarHostState,
                        releasesLazyListState = releasesLazyListState,
                        releasesLazyPagingItems = releasesLazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        onReleaseClick = onItemClick,
                        onPagedReleasesFlowChange = { pagedReleasesFlow = it }
                    )
                }

                AreaTab.PLACES -> {
                    PlacesByAreaScreen(
                        areaId = areaId,
                        filterText = filterText,
                        placesLazyListState = placesLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        placesLazyPagingItems = placesLazyPagingItems,
                        onPagedPlacesFlowChange = { pagedPlacesFlow = it },
                        onPlaceClick = onItemClick
                    )
                }

                AreaTab.STATS -> {
                    AreaStatsScreen(
                        areaId = areaId,
                        tabs = areaTabs.map { it.tab },
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    )
                }
            }
        }
    }
}
