package ly.david.mbjc.ui.artist.releases

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
internal fun ReleasesByArtistScreen(
    artistId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    releasesLazyListState: LazyListState = rememberLazyListState(),
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    onReleaseClick: (destination: Destination, String, String) -> Unit,
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit,
    filterText: String,
    showMoreInfo: Boolean,
    viewModel: ReleasesByArtistViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadPagedResources(artistId)
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
            onReleaseClick(Destination.LOOKUP_RELEASE, id, title)
        }
    )
}
