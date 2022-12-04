package ly.david.mbjc.ui.recording

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.RecordingScaffoldModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.paging.ReleasesListScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.recording.stats.RecordingStatsScreen

private enum class RecordingTab(@StringRes val titleRes: Int) {
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordingScaffold(
    recordingId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RECORDING
    val snackbarHostState = remember { SnackbarHostState() }

    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(RecordingTab.RELEASES) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var recording: RecordingScaffoldModel? by remember { mutableStateOf(null) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    LaunchedEffect(key1 = recordingId) {
        try {
            val recordingScaffoldModel = viewModel.lookupRecording(recordingId)
            if (titleWithDisambiguation.isNullOrEmpty()) {
                titleState = recordingScaffoldModel.getNameWithDisambiguation()
            }
            subtitleState = "Recording by ${recordingScaffoldModel.artistCredits.getDisplayNames()}"
            recording = recordingScaffoldModel

            viewModel.loadReleases(resourceId = recordingId)

            if (!recordedLookup) {
                viewModel.recordLookupHistory(
                    resourceId = recordingId,
                    resource = resource,
                    summary = titleState
                )
                recordedLookup = true
            }
        } catch (ex: Exception) {
            Log.d("Remove This", "RecordingScaffold: $ex")
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = recordingId)
                    CopyToClipboardMenuItem(recordingId)
                },
                subtitleDropdownMenuItems = {
                    recording?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                onItemClick(Destination.LOOKUP_ARTIST, artistCredit.artistId, null)
                            })
                    }
                },
                tabsTitles = RecordingTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = RecordingTab.values()[it] },
                showFilterIcon = selectedTab == RecordingTab.RELEASES,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(filterText)
                },
            )
        },
    ) { innerPadding ->

        val releasesLazyListState = rememberLazyListState()
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {

            RecordingTab.RELEASES -> {
                ReleasesListScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    onReleaseClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RELEASE, id, title)
                    }
                )
            }
            RecordingTab.RELATIONSHIPS -> {
                viewModel.loadRelations(recordingId)

                RelationsScreen(
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            RecordingTab.STATS -> {
                RecordingStatsScreen(recordingId = recordingId)
            }
        }
    }
}
