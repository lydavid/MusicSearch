package ly.david.mbjc.ui.work

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.WorkListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.work.details.WorkDetailsScreen
import ly.david.mbjc.ui.work.recordings.RecordingsByWorkScreen

private enum class WorkTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    RELATIONSHIPS(R.string.relationships),
    RECORDINGS(R.string.recordings),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WorkScaffold(
    workId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: WorkViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.WORK
    val snackbarHostState = remember { SnackbarHostState() }

    var titleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(WorkTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var work: WorkListItemModel? by remember { mutableStateOf(null) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    LaunchedEffect(key1 = workId) {
        try {
            val workListItemModel = viewModel.lookupWork(workId)
            if (titleWithDisambiguation.isNullOrEmpty()) {
                titleState = workListItemModel.getNameWithDisambiguation()
            }
            work = workListItemModel

        } catch (ex: Exception) {
            Log.d("Remove This", "WorkScaffold: $ex")
        }

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = workId,
                resource = resource,
                summary = titleState
            )
            recordedLookup = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = workId)
                    CopyToClipboardMenuItem(workId)
                },
                tabsTitles = WorkTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = WorkTab.values()[it] },
                showFilterIcon = selectedTab == WorkTab.RECORDINGS,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        val recordingsLazyListState = rememberLazyListState()
        var pagedRecordingsFlow: Flow<PagingData<RecordingListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel> =
            rememberFlowWithLifecycleStarted(pagedRecordingsFlow)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            WorkTab.DETAILS -> {
                val workListItemModel = work
                if (workListItemModel == null) {
                    FullScreenLoadingIndicator()
                } else {
                    WorkDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        work = workListItemModel,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            WorkTab.RELATIONSHIPS -> {
                viewModel.loadRelations(workId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            WorkTab.RECORDINGS -> {
                RecordingsByWorkScreen(
                    workId = workId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    recordingsLazyListState = recordingsLazyListState,
                    recordingsLazyPagingItems = recordingsLazyPagingItems,
                    onPagedRecordingsFlowChange = { pagedRecordingsFlow = it },
                    onRecordingClick = onItemClick,
                    filterText = filterText
                )
            }
            WorkTab.STATS -> {

            }
        }
    }
}
