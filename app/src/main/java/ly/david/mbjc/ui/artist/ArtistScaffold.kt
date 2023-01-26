package ly.david.mbjc.ui.artist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.artist.details.ArtistDetailsScreen
import ly.david.mbjc.ui.artist.releasegroups.ReleaseGroupsByArtistScreen
import ly.david.mbjc.ui.artist.releases.ReleasesByArtistScreen
import ly.david.mbjc.ui.artist.stats.ArtistStatsScreen
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

internal enum class ArtistTab(val tab: Tab) {
    DETAILS(Tab.DETAILS),
    RELEASE_GROUPS(Tab.RELEASE_GROUPS),
    RELEASES(Tab.RELEASES),
    RELATIONSHIPS(Tab.RELATIONSHIPS),
    STATS(Tab.STATS)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtistScaffold(
    artistId: String,
    titleWithDisambiguation: String? = null,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    onBack: () -> Unit,

    // This can be hoisted up which would normally let us preview this,
    // but because it relies on compose paging, we can't preview.
    viewModel: ArtistScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.ARTIST
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var isSorted by rememberSaveable { mutableStateOf(false) }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val artist by viewModel.artist.collectAsState()
    val showError by viewModel.isError.collectAsState()

    val showMoreInfoInReleaseListItem by viewModel.appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)

    LaunchedEffect(key1 = artistId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            artistId = artistId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                onBack = onBack,
                resource = resource,
                title = title,
                showFilterIcon = selectedTab in listOf(ArtistTab.RELEASE_GROUPS, ArtistTab.RELEASES),
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.ARTIST, resourceId = artistId)
                    CopyToClipboardMenuItem(artistId)

                    if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                        DropdownMenuItem(
                            text = {
                                Text(if (isSorted) "Un-sort" else "Sort")
                            },
                            onClick = {
                                closeMenu()
                                // TODO: disclaimer when turning on sort if we have not gotten all release groups
                                isSorted = !isSorted
                            }
                        )
                    }
                    // TODO: generalize switch menu item
                    if (selectedTab == ArtistTab.RELEASES) {
                        DropdownMenuItem(
                            text = { Text(if (showMoreInfoInReleaseListItem) "Show less info" else "Show more info") },
                            onClick = {
                                viewModel.appPreferences.setShowMoreInfoInReleaseListItem(!showMoreInfoInReleaseListItem)
                                closeMenu()
                            }
                        )
                    }
                },
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
                tabsTitles = ArtistTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
            )
        },
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
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ArtistTab.DETAILS -> {
                DetailsWithErrorHandling(
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = artist
                ) {
                    ArtistDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        artist = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artistId,
                    searchText = filterText,
                    isSorted = isSorted,
                    snackbarHostState = snackbarHostState,
                    onReleaseGroupClick = onItemClick,
                    lazyListState = releaseGroupsLazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems,
                    onPagedReleaseGroupsChange = {
                        pagedReleaseGroups = it
                    }
                )
            }
            ArtistTab.RELEASES -> {
                ReleasesByArtistScreen(
                    artistId = artistId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    releasesLazyListState = releasesLazyListState,
                    releasesLazyPagingItems = releasesLazyPagingItems,
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick,
                    filterText = filterText,
                    showMoreInfo = showMoreInfoInReleaseListItem
                )
            }
            ArtistTab.RELATIONSHIPS -> {
                viewModel.loadRelations(artistId)

                RelationsScreen(
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            ArtistTab.STATS -> {
                ArtistStatsScreen(
                    artistId = artistId,
                    tabs = ArtistTab.values().map { it.tab }
                )
            }
        }
    }
}
