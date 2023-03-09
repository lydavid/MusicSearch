package ly.david.mbjc.ui.work.recordings

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.RecordingsListScreen

@Composable
internal fun RecordingsByWorkScreen(
    workId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    recordingsLazyListState: LazyListState = rememberLazyListState(),
    recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    onRecordingClick: (entity: MusicBrainzResource, String, String) -> Unit,
    onPagedRecordingsFlowChange: (Flow<PagingData<RecordingListItemModel>>) -> Unit,
    filterText: String,
    viewModel: RecordingsByWorkViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = workId) {
        viewModel.loadPagedResources(workId)
        onPagedRecordingsFlowChange(viewModel.pagedResources)
    }
    viewModel.updateQuery(filterText)

    RecordingsListScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = recordingsLazyListState,
        lazyPagingItems = recordingsLazyPagingItems,
        onRecordingClick = { id, title ->
            onRecordingClick(MusicBrainzResource.RECORDING, id, title)
        }
    )
}
