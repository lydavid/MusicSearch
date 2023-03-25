package ly.david.mbjc.ui.collections.recordings

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
import ly.david.mbjc.ui.common.screen.RecordingsListScreen

@Composable
internal fun RecordingsByCollectionScreen(
    collectionId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    recordingsLazyListState: LazyListState = rememberLazyListState(),
    recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    onRecordingClick: (entity: MusicBrainzResource, String, String) -> Unit,
    onPagedRecordingsFlowChange: (Flow<PagingData<RecordingListItemModel>>) -> Unit,
    filterText: String,
    viewModel: RecordingsByCollectionViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = collectionId) {
        viewModel.loadPagedResources(collectionId)
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
