package ly.david.mbjc.ui.releasegroup

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.screen.RelationsScreen
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ToggleMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.releasegroup.details.ReleaseGroupDetailsScreen
import ly.david.mbjc.ui.releasegroup.releases.ReleasesByReleaseGroupScreen
import ly.david.mbjc.ui.releasegroup.stats.ReleaseGroupStatsScreen
import ly.david.ui.common.R

/**
 * Equivalent to a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Starts on a screen that displays all of its releases.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ReleaseGroupScaffold(
    releaseGroupId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    viewModel: ReleaseGroupScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE_GROUP
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()

    var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val releaseGroup by viewModel.releaseGroup.collectAsState()
    val showError by viewModel.isError.collectAsState()
    val url by viewModel.url.collectAsState()

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = ReleaseGroupTab.values()[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            releaseGroupId = releaseGroupId,
            selectedTab = selectedTab
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
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RELEASE_GROUP, resourceId = releaseGroupId)
                    CopyToClipboardMenuItem(releaseGroupId)
                    if (selectedTab == ReleaseGroupTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = R.string.show_more_info,
                            toggleOffText = R.string.show_less_info,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange
                        )
                    }
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, releaseGroupId)
                    }
                },
                subtitleDropdownMenuItems = {
                    releaseGroup?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                // Don't pass a title, because the name used here may not be the name used for the
                                // the artist's page.
                                onItemClick(MusicBrainzResource.ARTIST, artistCredit.artistId, null)
                            })
                    }
                },
                tabsTitles = ReleaseGroupTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                showFilterIcon = selectedTab in listOf(
                    ReleaseGroupTab.RELEASES,
                    ReleaseGroupTab.RELATIONSHIPS,
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        HorizontalPager(
            pageCount = ReleaseGroupTab.values().size,
            state = pagerState
        ) { page ->
            when (ReleaseGroupTab.values()[page]) {
                ReleaseGroupTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = releaseGroup
                    ) {
                        ReleaseGroupDetailsScreen(
                            releaseGroup = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            coverArtUrl = url,
                            lazyListState = detailsLazyListState,
                        )
                    }
                }

                ReleaseGroupTab.RELEASES -> {
                    ReleasesByReleaseGroupScreen(
                        releaseGroupId = releaseGroupId,
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

                ReleaseGroupTab.RELATIONSHIPS -> {
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

                ReleaseGroupTab.STATS -> {
                    ReleaseGroupStatsScreen(
                        releaseGroupId = releaseGroupId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = ReleaseGroupTab.values().map { it.tab }
                    )
                }
            }
        }
    }
}
