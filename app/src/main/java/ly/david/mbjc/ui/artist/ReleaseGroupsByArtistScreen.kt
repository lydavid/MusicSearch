package ly.david.mbjc.ui.artist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.ReleaseGroupUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@Composable
internal fun ReleaseGroupsByArtistScreen(
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
            } catch (ex: Exception) {
                // Technically, we could fallback to artist name from card in previous screen.
                "[Artist lookup failed]"
            }
        )
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateIsSorted(isSorted = isSorted)

    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.pagedReleaseGroups)
        .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is ReleaseGroupUiModel -> {
                ReleaseGroupCard(releaseGroup = uiModel) {
                    onReleaseGroupClick(id)
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
