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
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.ReleasesListScreen

@Composable
internal fun ReleasesByAreaScreen(
    areaId: String,
    filterText: String,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    releasesLazyListState: LazyListState = rememberLazyListState(),
    onReleaseClick: (destination: Destination, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
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
        onReleaseClick = { id, title ->
            onReleaseClick(Destination.LOOKUP_RELEASE, id, title)
        }
    )
}
