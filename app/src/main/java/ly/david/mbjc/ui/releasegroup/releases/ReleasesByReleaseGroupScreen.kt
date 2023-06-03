package ly.david.mbjc.ui.releasegroup.releases

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.release.ReleasesListScreen

@Composable
internal fun ReleasesByReleaseGroupScreen(
    releaseGroupId: String,
    filterText: String,
    showMoreInfo: Boolean,
    snackbarHostState: SnackbarHostState,
    releasesLazyListState: LazyListState,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    onReleaseClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
    viewModel: ReleasesByReleaseGroupViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.loadPagedResources(releaseGroupId)
        onPagedReleasesFlowChange(viewModel.pagedResources)
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
