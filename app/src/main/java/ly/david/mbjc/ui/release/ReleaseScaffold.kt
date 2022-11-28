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
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.domain.UiModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.release.details.ReleaseDetailsScreen
import ly.david.mbjc.ui.release.stats.ReleaseStatsScreen
import ly.david.mbjc.ui.release.tracks.TracksInReleaseScreen

private enum class ReleaseTab(@StringRes val titleRes: Int) {
    TRACKS(R.string.tracks),
    DETAILS(R.string.details),
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

    var title by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.TRACKS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var url: String by rememberSaveable { mutableStateOf("") }
    var release: ReleaseScaffoldModel? by remember { mutableStateOf(null) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = releaseId) {
        try {
            val releaseScaffoldModel = viewModel.lookupReleaseThenLoadTracks(releaseId)
            val coverArtUrl = releaseScaffoldModel.coverArtUrl
            if (coverArtUrl != null) {
                url = coverArtUrl
            } else if (releaseScaffoldModel.coverArtArchive.count > 0) {
                url = viewModel.getCoverArtUrlFromNetwork()
            }

            if (titleWithDisambiguation.isNullOrEmpty()) {
                title = releaseScaffoldModel.getNameWithDisambiguation()
            }
            subtitleState = "Release by ${releaseScaffoldModel.artistCredits.getDisplayNames()}"
            release = releaseScaffoldModel
        } catch (ex: Exception) {
            // If any of the above calls failed, we still want to update the release id so that
            // TracksInReleaseScreen will give us a Retry button.

            // TODO: when we fail any of the above calls, we will end up in a bad state
            //  where we have the title, but not the tracks/cover/subtitle
            //  fail more gracefully
            viewModel.loadTracks(releaseId)
        }

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = releaseId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
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
        val tracksLazyPagingItems: LazyPagingItems<UiModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedTracks)
                .collectAsLazyPagingItems()

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<UiModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ReleaseTab.TRACKS -> {
                TracksInReleaseScreen(
                    modifier = Modifier.padding(innerPadding),
                    coverArtUrl = url,
                    snackbarHostState = snackbarHostState,
                    lazyListState = tracksLazyListState,
                    lazyPagingItems = tracksLazyPagingItems,
                    onRecordingClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RECORDING, id, title)
                    }
                )
            }
            ReleaseTab.DETAILS -> {
                ReleaseDetailsScreen(
                    releaseScaffoldModel = release,
                    onLabelClick = {
                        onItemClick(Destination.LOOKUP_LABEL, id, name)
                    },
                    onAreaClick = {
                        onItemClick(Destination.LOOKUP_AREA, id, name)
                    },
                    lazyListState = detailsLazyListState
                )
            }
            ReleaseTab.RELATIONSHIPS -> {
                viewModel.loadRelations(releaseId)

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
                    releaseScaffoldModel = release
                )
            }
        }
    }
}
