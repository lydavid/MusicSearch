package ly.david.mbjc.ui.artist.releasegroups

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.releasegroup.ReleaseGroupsListScreen

@Composable
internal fun ReleaseGroupsByArtistScreen(
    artistId: String,
    modifier: Modifier,
    searchText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onReleaseGroupClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedReleaseGroupsChange: (Flow<PagingData<ListItemModel>>) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadPagedResources(artistId)
        onPagedReleaseGroupsChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateSorted(sorted = isSorted)

    ReleaseGroupsListScreen(
        lazyListState = lazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = lazyPagingItems,
        onReleaseGroupClick = onReleaseGroupClick
    )
}
