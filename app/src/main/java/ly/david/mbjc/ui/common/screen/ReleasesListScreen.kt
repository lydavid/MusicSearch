package ly.david.mbjc.ui.common.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.release.ReleaseListItem
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReleasesListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    showMoreInfo: Boolean = true,
    onReleaseClick: (String, String) -> Unit,
    viewModel: ReleasesListViewModel = hiltViewModel()
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { releaseListItemModel: ReleaseListItemModel? ->
        when (releaseListItemModel) {
            is ReleaseListItemModel -> {
                ReleaseListItem(
                    release = releaseListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                    showMoreInfo = showMoreInfo,
                    requestForMissingCoverArtPath = {
                        try {
                            viewModel.getReleaseCoverArtPathFromNetwork(releaseId = releaseListItemModel.id)
                        } catch (ex: Exception) {
                            Timber.e(ex)
                        }
                    }
                ) {
                    onReleaseClick(id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
