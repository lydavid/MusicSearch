package ly.david.mbjc.ui.work

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
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.screen.RelationsScreen
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.work.details.WorkDetailsScreen
import ly.david.mbjc.ui.work.recordings.RecordingsByWorkScreen
import ly.david.mbjc.ui.work.stats.WorkGroupStatsScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun WorkScaffold(
    workId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
    viewModel: WorkScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.WORK
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()

    var selectedTab by rememberSaveable { mutableStateOf(WorkTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val work by viewModel.work.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = workId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = WorkTab.values()[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            workId = workId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = workId)
                    CopyToClipboardMenuItem(workId)
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, workId)
                    }
                },
                tabsTitles = WorkTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                showFilterIcon = selectedTab == WorkTab.RECORDINGS,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        val recordingsLazyListState = rememberLazyListState()
        var pagedRecordingsFlow: Flow<PagingData<RecordingListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel> =
            rememberFlowWithLifecycleStarted(pagedRecordingsFlow)
                .collectAsLazyPagingItems()

        HorizontalPager(
            pageCount = WorkTab.values().size,
            state = pagerState
        ) { page ->
            when (WorkTab.values()[page]) {
                WorkTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = work
                    ) {
                        WorkDetailsScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            work = it,
                            lazyListState = detailsLazyListState
                        )
                    }
                }
                WorkTab.RELATIONSHIPS -> {
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
                WorkTab.RECORDINGS -> {
                    // TODO: browsing rather than lookup recording-rels doesn't include attributes
                    //  Compare:
                    //  - https://musicbrainz.org/ws/2/work/c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3?inc=recording-rels
                    //  - https://musicbrainz.org/ws/2/recording?work=c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3
                    //      - missing "instrumental" attribute
                    RecordingsByWorkScreen(
                        workId = workId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        recordingsLazyListState = recordingsLazyListState,
                        recordingsLazyPagingItems = recordingsLazyPagingItems,
                        onPagedRecordingsFlowChange = { pagedRecordingsFlow = it },
                        onRecordingClick = onItemClick,
                        filterText = filterText
                    )
                }
                WorkTab.STATS -> {
                    WorkGroupStatsScreen(
                        workId = workId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = WorkTab.values().map { it.tab }
                    )
                }
            }
        }
    }
}
