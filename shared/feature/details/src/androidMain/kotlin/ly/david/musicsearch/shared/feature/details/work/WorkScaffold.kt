package ly.david.musicsearch.shared.feature.details.work

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.work.details.WorkDetailsScreen
import ly.david.musicsearch.shared.feature.details.work.recordings.RecordingsByWorkScreen
import ly.david.musicsearch.shared.feature.details.work.stats.WorkGroupStatsScreen
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.relation.RelationsListScreen
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun WorkScaffold(
    workId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
    viewModel: WorkScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.WORK
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = WorkTab.values()::size)

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

    LaunchedEffect(
        key1 = selectedTab,
        key2 = forceRefresh
    ) {
        viewModel.loadDataForTab(
            workId = workId,
            selectedTab = selectedTab,
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                entity = resource,
                title = title,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        entity = resource,
                        entityId = workId
                    )
                    CopyToClipboardMenuItem(workId)
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(
                            resource,
                            workId
                        )
                    }
                },
                showFilterIcon = selectedTab !in listOf(
                    WorkTab.STATS,
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = WorkTab.values().map { it.tab.getTitle(strings) },
                        selectedTabIndex = selectedTab.ordinal,
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        val recordingsLazyListState = rememberLazyListState()
        var pagedRecordingsFlow: Flow<PagingData<RecordingListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel> =
            rememberFlowWithLifecycleStarted(pagedRecordingsFlow)
                .collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (WorkTab.values()[page]) {
                WorkTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = work,
                    ) {
                        WorkDetailsScreen(
                            work = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = filterText,
                            lazyListState = detailsLazyListState,
                            onItemClick = onItemClick,
                        )
                    }
                }

                WorkTab.RELATIONSHIPS -> {
                    viewModel.updateQuery(filterText)
                    RelationsListScreen(
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
                        filterText = filterText,
                    )
                }

                WorkTab.STATS -> {
                    WorkGroupStatsScreen(
                        workId = workId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = WorkTab.values().map { it.tab }.toImmutableList(),
                    )
                }
            }
        }
    }
}