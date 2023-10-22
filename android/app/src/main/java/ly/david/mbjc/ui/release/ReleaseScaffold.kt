package ly.david.mbjc.ui.release

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
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.mbjc.ui.release.details.ReleaseDetailsScreen
import ly.david.mbjc.ui.release.stats.ReleaseStatsScreen
import ly.david.mbjc.ui.release.tracks.TracksByReleaseScreen
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.relation.RelationsListScreen
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import org.koin.androidx.compose.koinViewModel

/**
 * The top-level screen for a release.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ReleaseScaffold(
    releaseId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
    viewModel: ReleaseScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.RELEASE
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = ReleaseTab.values()::size)

    var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val url by viewModel.url.collectAsState()
    val release by viewModel.release.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = releaseId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = ReleaseTab.values()[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            releaseId = releaseId,
            selectedTab = selectedTab,
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
                    OpenInBrowserMenuItem(entity = MusicBrainzEntity.RELEASE, entityId = releaseId)
                    CopyToClipboardMenuItem(entityId = releaseId)
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, releaseId)
                    }
                },
                subtitleDropdownMenuItems = {
                    release?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(MusicBrainzEntity.ARTIST, artistCredit.artistId, null)
                            },
                        )
                    }
                    release?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(
                            text = { Text(text = releaseGroup.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                onItemClick(MusicBrainzEntity.RELEASE_GROUP, releaseGroup.id, null)
                            },
                        )
                    }
                },
                showFilterIcon = selectedTab !in listOf(
                    ReleaseTab.STATS,
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = ReleaseTab.values().map { it.tab.getTitle(strings) },
                        selectedTabIndex = selectedTab.ordinal,
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val tracksLazyListState = rememberLazyListState()
        var pagedTracksFlow: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val tracksLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(pagedTracksFlow)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations).collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (ReleaseTab.values()[page]) {
                ReleaseTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        // TODO: [low] if you spam click this it won't work
                        //  but you can always change tabs or come back to reload
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = release,
                    ) {
                        // TODO: test refreshing this screen
                        //  want to see if deleting labels by release will cascade delete its junction table
                        ReleaseDetailsScreen(
                            release = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = filterText,
                            coverArtUrl = url,
                            onItemClick = onItemClick,
                            lazyListState = detailsLazyListState,
                        )
                    }
                }

                ReleaseTab.TRACKS -> {
                    TracksByReleaseScreen(
                        releaseId = releaseId,
                        filterText = filterText,
                        lazyListState = tracksLazyListState,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyPagingItems = tracksLazyPagingItems,
                        onPagedTracksFlowChange = { pagedTracksFlow = it },
                        onRecordingClick = { id, title ->
                            onItemClick(MusicBrainzEntity.RECORDING, id, title)
                        },
                    )
                }

                ReleaseTab.RELATIONSHIPS -> {
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

                ReleaseTab.STATS -> {
                    ReleaseStatsScreen(
                        releaseId = releaseId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = ReleaseTab.values().map { it.tab }.toImmutableList(),
                    )
                }
            }
        }
    }
}
