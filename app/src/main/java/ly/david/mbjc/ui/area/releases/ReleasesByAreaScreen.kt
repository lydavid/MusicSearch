package ly.david.mbjc.ui.area.releases

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
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.screen.ReleasesListScreen

@Composable
internal fun ReleasesByAreaScreen(
    areaId: String,
    filterText: String,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    releasesLazyListState: LazyListState = rememberLazyListState(),
    onReleaseClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
    showMoreInfo: Boolean,
    viewModel: ReleasesByAreaViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = areaId) {
        viewModel.loadPagedResources(areaId)
        onPagedReleasesFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    ReleasesListScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = releasesLazyListState,
        lazyPagingItems = releasesLazyPagingItems,
        showMoreInfo = showMoreInfo,
        onReleaseClick = { id, title ->
            onReleaseClick(MusicBrainzResource.RELEASE, id, title)
        }
    )
}
