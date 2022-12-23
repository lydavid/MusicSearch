package ly.david.mbjc.ui.release

import androidx.annotation.StringRes
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
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.release.details.ReleaseDetailsScreen
import ly.david.mbjc.ui.release.stats.ReleaseStatsScreen
import ly.david.mbjc.ui.release.tracks.TracksInReleaseScreen

internal enum class ReleaseTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    TRACKS(R.string.tracks),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

/**
 * Equivalent of a screen like: https://musicbrainz.org/release/f171e0ae-bea8-41e6-bb41-4c7af7977f50
 *
 * Starts on a screen that displays all of its media/tracks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseScaffold(
    releaseId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: ReleaseViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE

    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title = viewModel.title.collectAsState().value
    val subtitleState = viewModel.subtitleState.collectAsState().value
    val url = viewModel.url.collectAsState()
    val release = viewModel.release.collectAsState()
    val showError = viewModel.isError.collectAsState()

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                subtitle = subtitleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RELEASE, resourceId = releaseId)
                    CopyToClipboardMenuItem(resourceId = releaseId)
                },
                subtitleDropdownMenuItems = {
                    release.value?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(Destination.LOOKUP_ARTIST, artistCredit.artistId, null)
                            })
                    }
                    release.value?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(
                            text = { Text(text = releaseGroup.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                onItemClick(Destination.LOOKUP_RELEASE_GROUP, releaseGroup.id, null)
                            })
                    }
                },
                tabsTitles = ReleaseTab.values().map { stringResource(id = it.titleRes) },
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

        val tracksLazyListState = rememberLazyListState()
        val tracksLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedTracks)
                .collectAsLazyPagingItems()

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ReleaseTab.DETAILS -> {
                val releaseScaffoldModel = release.value
                when {
                    showError.value -> {
                        FullScreenErrorWithRetry(
                            // TODO: [low] if you spam click this it won't work
                            //  but you can always change tabs or come back to reload
                            onClick = { forceRefresh = true }
                        )
                    }
                    releaseScaffoldModel == null -> {
                        FullScreenLoadingIndicator()
                    }
                    else -> {
                        ReleaseDetailsScreen(
                            releaseScaffoldModel = releaseScaffoldModel,
                            coverArtUrl = url.value,
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
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            ReleaseTab.STATS -> {
                ReleaseStatsScreen(
                    releaseId = releaseId,
                )
            }
        }
    }
}
