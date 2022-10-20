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
import ly.david.data.domain.UiModel
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@Composable
internal fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    onReleaseGroupClick: (String) -> Unit = {},
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<UiModel>,
    onPagedReleaseGroupsChange: (Flow<PagingData<UiModel>>) -> Unit,
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = artistId) {
        viewModel.updateArtistId(artistId)
        onPagedReleaseGroupsChange(viewModel.pagedReleaseGroups)
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateIsSorted(isSorted = isSorted)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is ly.david.data.domain.ReleaseGroupUiModel -> {
                ReleaseGroupCard(releaseGroup = uiModel) {
                    onReleaseGroupClick(id)
                }
            }
            is ly.david.data.domain.ListSeparator -> {
                ListSeparatorHeader(text = uiModel.text)
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
