package ly.david.mbjc.ui.work.recordings

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.ui.common.recording.RecordingsListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecordingsByWorkScreen(
    workId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    recordingsLazyListState: LazyListState = rememberLazyListState(),
    recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    onRecordingClick: (entity: MusicBrainzEntity, String, String) -> Unit,
    onPagedRecordingsFlowChange: (Flow<PagingData<RecordingListItemModel>>) -> Unit,
    filterText: String,
    viewModel: RecordingsByWorkViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = workId) {
        viewModel.loadPagedEntities(workId)
        onPagedRecordingsFlowChange(viewModel.pagedEntities)
    }
    viewModel.updateQuery(filterText)

    RecordingsListScreen(
        lazyListState = recordingsLazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = recordingsLazyPagingItems,
        onRecordingClick = onRecordingClick,
    )
}
