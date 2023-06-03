package ly.david.mbjc.ui.artist.releases

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
internal fun ReleasesByArtistScreen(
    artistId: String,
    filterText: String,
    showMoreInfo: Boolean,
    snackbarHostState: SnackbarHostState,
    releasesLazyListState: LazyListState,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    onReleaseClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
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
        onReleaseClick = onReleaseClick
    )
}
