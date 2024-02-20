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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.mbjc.ui.releasegroup.details.ReleaseGroupDetailsScreen
import ly.david.mbjc.ui.releasegroup.releases.ReleasesByReleaseGroupScreen
import ly.david.mbjc.ui.releasegroup.stats.ReleaseGroupStatsScreen
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.ui.common.relation.RelationsListScreen
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TabsBar
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.topappbar.getTitle
import org.koin.androidx.compose.koinViewModel

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
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    viewModel: ReleaseGroupScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.RELEASE_GROUP
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = ReleaseGroupTab.values()::size)

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
                    OpenInBrowserMenuItem(entity = MusicBrainzEntity.RELEASE_GROUP, entityId = releaseGroupId)
                    CopyToClipboardMenuItem(releaseGroupId)
                    if (selectedTab == ReleaseGroupTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange,
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
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntity.ARTIST) },
                            onClick = {
                                closeMenu()
                                // Don't pass a title, because the name used here may not be the name used for the
                                // the artist's page.
                                onItemClick(MusicBrainzEntity.ARTIST, artistCredit.artistId, null)
                            },
                        )
                    }
                },
                showFilterIcon = selectedTab !in listOf(
                    ReleaseGroupTab.STATS,
                ),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = ReleaseGroupTab.values().map { it.tab.getTitle(strings) },
                        selectedTabIndex = selectedTab.ordinal,
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
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
        val relationsLazyPagingItems =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (ReleaseGroupTab.values()[page]) {
                ReleaseGroupTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = releaseGroup,
                    ) {
                        ReleaseGroupDetailsScreen(
                            releaseGroup = it,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = filterText,
                            coverArtUrl = url,
                            lazyListState = detailsLazyListState,
                            onItemClick = onItemClick,
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
                        onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    )
                }

                ReleaseGroupTab.RELATIONSHIPS -> {
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

                ReleaseGroupTab.STATS -> {
                    ReleaseGroupStatsScreen(
                        releaseGroupId = releaseGroupId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        tabs = ReleaseGroupTab.values().map { it.tab }.toImmutableList(),
                    )
                }
            }
        }
    }
}
