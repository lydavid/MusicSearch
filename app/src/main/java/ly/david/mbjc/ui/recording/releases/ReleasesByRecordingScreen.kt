package ly.david.mbjc.ui.recording.releases

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.release.ReleasesListScreen

@Composable
internal fun ReleasesByRecordingScreen(
    recordingId: String,
    filterText: String,
    showMoreInfo: Boolean,
    snackbarHostState: SnackbarHostState,
    releasesLazyListState: LazyListState,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
    viewModel: ReleasesByRecordingViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = recordingId) {
        viewModel.loadPagedEntities(recordingId)
        onPagedReleasesFlowChange(viewModel.pagedEntities)
    }
    viewModel.updateQuery(filterText)

    ReleasesListScreen(
        lazyListState = releasesLazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = releasesLazyPagingItems,
        showMoreInfo = showMoreInfo,
        onReleaseClick = onReleaseClick
    )
}
