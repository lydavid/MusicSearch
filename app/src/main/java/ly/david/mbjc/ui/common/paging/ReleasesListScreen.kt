package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.ui.release.ReleaseCard

@Composable
internal fun ReleasesListScreen(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    onReleaseClick: (String) -> Unit = {},
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ReleaseUiModel>
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { releaseUiModel: ReleaseUiModel? ->
        when (releaseUiModel) {
            is ReleaseUiModel -> {
                ReleaseCard(releaseUiModel = releaseUiModel) {
                    onReleaseClick(id)
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
