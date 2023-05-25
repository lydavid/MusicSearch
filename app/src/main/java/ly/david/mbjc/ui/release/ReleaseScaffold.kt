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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.ResourceIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.screen.RelationsScreen
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.release.details.ReleaseDetailsScreen
import ly.david.mbjc.ui.release.stats.ReleaseStatsScreen
import ly.david.mbjc.ui.release.tracks.TracksInReleaseScreen

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
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
    viewModel: ReleaseScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()

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
            releaseId = releaseId, selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                subtitle = subtitle,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RELEASE, resourceId = releaseId)
                    CopyToClipboardMenuItem(resourceId = releaseId)
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, releaseId)
                    }
                },
                subtitleDropdownMenuItems = {
                    release?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(MusicBrainzResource.ARTIST, artistCredit.artistId, null)
                            })
                    }
                    release?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(text = { Text(text = releaseGroup.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                onItemClick(MusicBrainzResource.RELEASE_GROUP, releaseGroup.id, null)
                            })
                    }
                },
                tabsTitles = ReleaseTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                showFilterIcon = selectedTab in listOf(
                    ReleaseTab.TRACKS,
                    ReleaseTab.RELATIONSHIPS
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val tracksLazyListState = rememberLazyListState()
        val tracksLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedTracks).collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations).collectAsLazyPagingItems()

        HorizontalPager(
            pageCount = ReleaseTab.values().size, state = pagerState
        ) { page ->
            when (ReleaseTab.values()[page]) {
                ReleaseTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        // TODO: [low] if you spam click this it won't work
                        //  but you can always change tabs or come back to reload
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = release
                    ) {
                        // TODO: test refreshing this screen
                        //  want to see if deleting labels by release will cascade delete its junction table
                        ReleaseDetailsScreen(
                            release = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            coverArtUrl = url,
                            onLabelClick = {
                                onItemClick(MusicBrainzResource.LABEL, id, name)
                            },
                            onAreaClick = {
                                onItemClick(MusicBrainzResource.AREA, id, name)
                            },
                            lazyListState = detailsLazyListState,
                        )
                    }
                }

                ReleaseTab.TRACKS -> {
                    TracksInReleaseScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        snackbarHostState = snackbarHostState,
                        lazyListState = tracksLazyListState,
                        lazyPagingItems = tracksLazyPagingItems,
                        onRecordingClick = { id, title ->
                            onItemClick(MusicBrainzResource.RECORDING, id, title)
                        }
                    )
                }

                ReleaseTab.RELATIONSHIPS -> {
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

                ReleaseTab.STATS -> {
                    ReleaseStatsScreen(releaseId = releaseId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = ReleaseTab.values().map { it.tab })
                }
            }
        }
    }
}
