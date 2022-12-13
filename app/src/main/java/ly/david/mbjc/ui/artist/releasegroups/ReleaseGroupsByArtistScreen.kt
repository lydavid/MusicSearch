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
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupListItem

@Composable
internal fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    onReleaseGroupClick: (String) -> Unit = {},
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

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ReleaseGroupListItemModel -> {
                ReleaseGroupListItem(releaseGroup = listItemModel) {
                    onReleaseGroupClick(id)
                }
            }
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
