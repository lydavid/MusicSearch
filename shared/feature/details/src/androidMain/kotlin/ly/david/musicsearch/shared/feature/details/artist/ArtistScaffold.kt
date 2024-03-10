package ly.david.musicsearch.shared.feature.details.artist

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
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.feature.details.artist.details.ArtistDetailsScreen
import ly.david.musicsearch.shared.feature.details.artist.releasegroups.ReleaseGroupsByArtistScreen
import ly.david.musicsearch.shared.feature.details.artist.releases.ReleasesByArtistScreen
import ly.david.musicsearch.shared.feature.details.artist.stats.ArtistStatsScreen
import ly.david.musicsearch.strings.LocalStrings
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistScaffold(
    artistId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onBack: () -> Unit = {},
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    viewModel: ArtistScaffoldViewModel = koinViewModel(),
) {
    val resource = MusicBrainzEntity.ARTIST
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = ArtistTab.values()::size)

    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.DETAILS) }

    val title by viewModel.title.collectAsState()
    val artist by viewModel.artist.collectAsState()
    val url by viewModel.url.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = artistId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTab = ArtistTab.values()[pagerState.currentPage]
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.loadDataForTab(
            artistId = artistId,
            selectedTab = selectedTab,
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                onBack = onBack,
                entity = resource,
                title = title,
                scrollBehavior = scrollBehavior,
                showFilterIcon = selectedTab !in listOf(
                    ArtistTab.STATS,
                ),
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(entity = MusicBrainzEntity.ARTIST, entityId = artistId)
                    CopyToClipboardMenuItem(artistId)
                    if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = sortReleaseGroupListItems,
                            onToggle = onSortReleaseGroupListItemsChange,
                        )
                    }
                    if (selectedTab == ArtistTab.RELEASES) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange,
                        )
                    }
                    AddToCollectionMenuItem {
                        onAddToCollectionMenuClick(resource, artistId)
                    }
                },
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = ArtistTab.values().map { it.tab.getTitle(strings) },
                        selectedTabIndex = selectedTab.ordinal,
                        onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        // This is sufficient to preserve scroll position when switching tabs
        val releaseGroupsLazyListState = rememberLazyListState()
        var pagedReleaseGroups: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releaseGroupsLazyPagingItems = rememberFlowWithLifecycleStarted(pagedReleaseGroups)
            .collectAsLazyPagingItems()

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
            when (ArtistTab.values()[page]) {
                ArtistTab.DETAILS -> {
                    DetailsWithErrorHandling(
                        modifier = Modifier.padding(innerPadding),
                        showError = showError,
                        onRetryClick = { forceRefresh = true },
                        scaffoldModel = artist,
                    ) { artist ->
                        ArtistDetailsScreen(
                            artist = artist,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            filterText = filterText,
                            artistImageUrl = url,
                            lazyListState = detailsLazyListState,
                            onItemClick = onItemClick,
                        )
                    }
                }

                ArtistTab.RELEASE_GROUPS -> {
                    ReleaseGroupsByArtistScreen(
                        artistId = artistId,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        filterText = filterText,
                        isSorted = sortReleaseGroupListItems,
                        snackbarHostState = snackbarHostState,
                        onReleaseGroupClick = onItemClick,
                        lazyListState = releaseGroupsLazyListState,
                        lazyPagingItems = releaseGroupsLazyPagingItems,
                        onPagedReleaseGroupsChange = {
                            pagedReleaseGroups = it
                        },
                    )
                }

                ArtistTab.RELEASES -> {
                    ReleasesByArtistScreen(
                        artistId = artistId,
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

                ArtistTab.RELATIONSHIPS -> {
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

                ArtistTab.STATS -> {
                    ArtistStatsScreen(
                        artistId = artistId,
                        tabs = ArtistTab.values().map { it.tab }.toImmutableList(),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                    )
                }
            }
        }
    }
}
