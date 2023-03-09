package ly.david.mbjc.ui.area

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.details.AreaDetailsScreen
import ly.david.mbjc.ui.area.places.PlacesByAreaScreen
import ly.david.mbjc.ui.area.releases.ReleasesByAreaScreen
import ly.david.mbjc.ui.area.stats.AreaStatsScreen
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

/**
 * The top-level screen for an area.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
internal fun AreaScaffold(
    areaId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: () -> Unit = {},
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
        viewModel.onSelectedTabChange(
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
                    AddToCollectionMenuItem(onClick = onAddToCollectionMenuClick)
                },
                tabsTitles = areaTabs.map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = areaTabs.indexOf(selectedTab),
                onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                showFilterIcon = selectedTab in listOf(AreaTab.RELEASES, AreaTab.PLACES),
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
            count = areaTabs.size,
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
                    RelationsScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        onItemClick = onItemClick,
                        lazyListState = relationsLazyListState,
                        lazyPagingItems = relationsLazyPagingItems,
                    )
                }
                AreaTab.RELEASES -> {
                    ReleasesByAreaScreen(
                        areaId = areaId,
                        filterText = filterText,
                        releasesLazyPagingItems = releasesLazyPagingItems,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        releasesLazyListState = releasesLazyListState,
                        onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                        onReleaseClick = onItemClick
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
