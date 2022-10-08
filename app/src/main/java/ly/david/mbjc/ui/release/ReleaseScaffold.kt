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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.release.relations.ReleaseRelationsScreen
import ly.david.mbjc.ui.release.tracks.TracksInReleaseScreen

private enum class ReleaseTab(@StringRes val titleRes: Int) {
    TRACKS(R.string.tracks),
    DETAILS(R.string.details),
    RELATIONSHIPS(R.string.relationships),

    // TODO: release events

    STATS(R.string.stats)
}

/**
 * Equivalent of a screen like: https://musicbrainz.org/release/f171e0ae-bea8-41e6-bb41-4c7af7977f50
 *
 * Displays the tracks/recordings for this release.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseScaffold(
    releaseId: String,
    onBack: () -> Unit,
    titleWithDisambiguation: String? = null,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: ReleaseViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var title by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.TRACKS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var coverArtUrl: String by rememberSaveable { mutableStateOf("") }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = releaseId) {
        viewModel.updateReleaseId(releaseId)

        val release = viewModel.lookupRelease(releaseId)
        if (release is ReleaseRoomModel && release.coverArtUrl != null) {
            coverArtUrl = release.coverArtUrl
        } else if (release.coverArtArchive.count > 0) {
            coverArtUrl = viewModel.getCoverArtUrlFromNetwork()
        }

        if (titleWithDisambiguation.isNullOrEmpty()) {
            title = release.getNameWithDisambiguation()
        }
        subtitleState = "Release by [TODO]"

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = releaseId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
    }
//    LaunchedEffect(key1 = releaseId) {
//
//    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                resource = resource,
                title = title,
                subtitle = subtitleState,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(resource, releaseId)
                            closeMenu()
                        }
                    )
                },
                tabsTitles = ReleaseTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseTab.values()[it] },
                showSearchIcon = selectedTab == ReleaseTab.TRACKS,
                searchText = filterText,
                onSearchTextChange = {
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

        val relationsLazyListState = rememberLazyListState()
        var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
        val relationsLazyPagingItems: LazyPagingItems<UiModel> =
            rememberFlowWithLifecycleStarted(pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ReleaseTab.TRACKS -> {
                TracksInReleaseScreen(
                    modifier = Modifier.padding(innerPadding),
                    coverArtUrl = coverArtUrl,
                    snackbarHostState = snackbarHostState,
                    lazyListState = tracksLazyListState,
                    lazyPagingItems = tracksLazyPagingItems,
                    onRecordingClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RECORDING, id, title)
                    }
                )
            }
            ReleaseTab.DETAILS -> {

            }
            ReleaseTab.RELATIONSHIPS -> {
                ReleaseRelationsScreen(
                    releaseId = releaseId,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                    onPagedRelationsChange = {
                        pagedRelations = it
                    }
                )
            }
            ReleaseTab.STATS -> {

            }
        }
    }
}
