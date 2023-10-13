package ly.david.mbjc.ui.recording

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.mbjc.ui.recording.details.RecordingDetailsScreen
import ly.david.mbjc.ui.recording.releases.ReleasesByRecordingScreen
import ly.david.mbjc.ui.recording.stats.RecordingStatsScreen
import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.relation.RelationsScreen
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun RecordingScaffold(
    recordingId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    viewModel: RecordingScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.RECORDING
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = RecordingTab.values()::size)

    var selectedTab by rememberSaveable { mutableStateOf(RecordingTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val recording by viewModel.recording.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = recordingId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = RecordingTab.values()[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            recordingId = recordingId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                entity = resource,
                title = title,
                subtitle = subtitle,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(entity = resource, entityId = recordingId)
                    CopyToClipboardMenuItem(recordingId)
                    if (selectedTab == RecordingTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange
                        )
                    }
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, recordingId)
                    }
                },
                subtitleDropdownMenuItems = {
                    recording?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(MusicBrainzEntity.ARTIST, artistCredit.artistId, null)
                            }
                        )
                    }
                },
                showFilterIcon = selectedTab !in listOf(
                    RecordingTab.STATS,
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = RecordingTab.values().map { it.tab.getTitle(strings) },
                        selectedTabIndex = selectedTab.ordinal,
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } }
                    )
                }
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState
        ) { page ->
            when (RecordingTab.values()[page]) {
                RecordingTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = recording
                    ) {
                        RecordingDetailsScreen(
                            recording = it,
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

                RecordingTab.RELEASES -> {
                    ReleasesByRecordingScreen(
                        recordingId = recordingId,
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

                RecordingTab.RELATIONSHIPS -> {
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

                RecordingTab.STATS -> {
                    RecordingStatsScreen(
                        recordingId = recordingId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = RecordingTab.values().map { it.tab }.toImmutableList(),
                    )
                }
            }
        }
    }
}
