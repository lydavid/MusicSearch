package ly.david.mbjc.ui.artist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.domain.ReleaseGroupUiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    onReleaseGroupClick: (String) -> Unit = {},
    onTitleUpdate: (title: String) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = artistId) {
        viewModel.updateArtistId(artistId)
        onTitleUpdate(
            try {
                viewModel.lookupArtist(artistId).getNameWithDisambiguation()
            } catch (e: Exception) {
                // Technically, we could fallback to artist name from card in previous screen.
                "[Artist lookup failed]"
            }
        )
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateIsSorted(isSorted = isSorted)

    val lazyPagingItems: LazyPagingItems<UiModel> = viewModel.pagedReleaseGroups.collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is ReleaseGroupUiModel -> {
                ReleaseGroupCard(releaseGroup = uiModel) {
                    onReleaseGroupClick(it.id)
                }
            }
            is ListSeparator -> {
                ListSeparatorHeader(text = uiModel.text)
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
