package ly.david.mbjc.ui.recording

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
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.recording.details.RecordingDetailsScreen
import ly.david.mbjc.ui.recording.releases.ReleasesByRecordingScreen
import ly.david.mbjc.ui.recording.stats.RecordingStatsScreen

internal enum class RecordingTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
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
    viewModel: RecordingScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RECORDING
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(RecordingTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val recording by viewModel.recording.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = recordingId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            recordingId = recordingId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                subtitle = subtitle,
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
                },
            )
        },
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

        when (selectedTab) {
            RecordingTab.DETAILS -> {
                DetailsWithErrorHandling(
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = recording
                ) {
                    RecordingDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        recording = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            RecordingTab.RELEASES -> {
                ReleasesByRecordingScreen(
                    recordingId = recordingId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    releasesLazyListState = releasesLazyListState,
                    releasesLazyPagingItems = releasesLazyPagingItems,
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick,
                    filterText = filterText
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
