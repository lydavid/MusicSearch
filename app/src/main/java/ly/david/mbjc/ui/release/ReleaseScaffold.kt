package ly.david.mbjc.ui.release

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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.release.details.ReleaseDetailsScreen
import ly.david.mbjc.ui.release.stats.ReleaseStatsScreen
import ly.david.mbjc.ui.release.tracks.TracksInReleaseScreen

internal enum class ReleaseTab(val tab: Tab) {
    DETAILS(Tab.DETAILS),
    TRACKS(Tab.TRACKS),
    RELATIONSHIPS(Tab.RELATIONSHIPS),
    STATS(Tab.STATS)
}

/**
 * The top-level screen for a release.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseScaffold(
    releaseId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: ReleaseScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE
    val snackbarHostState = remember { SnackbarHostState() }
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

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            releaseId = releaseId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RELEASE, resourceId = releaseId)
                    CopyToClipboardMenuItem(resourceId = releaseId)
                },
                subtitleDropdownMenuItems = {
                    release?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(Destination.LOOKUP_ARTIST, artistCredit.artistId, null)
                            })
                    }
                    release?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(
                            text = { Text(text = releaseGroup.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                onItemClick(Destination.LOOKUP_RELEASE_GROUP, releaseGroup.id, null)
                            })
                    }
                },
                tabsTitles = ReleaseTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseTab.values()[it] },
                showFilterIcon = selectedTab == ReleaseTab.TRACKS,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val tracksLazyListState = rememberLazyListState()
        val tracksLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedTracks)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
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
                        modifier = Modifier.padding(innerPadding),
                        coverArtUrl = url,
                        onLabelClick = {
                            onItemClick(Destination.LOOKUP_LABEL, id, name)
                        },
                        onAreaClick = {
                            onItemClick(Destination.LOOKUP_AREA, id, name)
                        },
                        lazyListState = detailsLazyListState,
                    )
                }
            }
            ReleaseTab.TRACKS -> {
                TracksInReleaseScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    lazyListState = tracksLazyListState,
                    lazyPagingItems = tracksLazyPagingItems,
                    onRecordingClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RECORDING, id, title)
                    }
                )
            }
            ReleaseTab.RELATIONSHIPS -> {
                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            ReleaseTab.STATS -> {
                ReleaseStatsScreen(
                    releaseId = releaseId,
                    modifier = Modifier.padding(innerPadding),
                    tabs = ReleaseTab.values().map { it.tab }
                )
            }
        }
    }
}
