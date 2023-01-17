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
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.ReleaseGroupsListScreen

@Composable
internal fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    onReleaseGroupClick: (destination: Destination, String, String) -> Unit,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onPagedReleaseGroupsChange: (Flow<PagingData<ListItemModel>>) -> Unit,
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadPagedResources(artistId)
        onPagedReleaseGroupsChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateSorted(sorted = isSorted)

    ReleaseGroupsListScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        onReleaseGroupClick = { id, title ->
            onReleaseGroupClick(Destination.LOOKUP_RELEASE_GROUP, id, title)
        }
    )
}
