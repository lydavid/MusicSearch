package ly.david.mbjc.ui.label.releases

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.release.ReleaseCard

// TODO: the only difference between this and ReleasesByReleaseGroupScreen is it uses
//  labelId and it doesn't have a subtitle
@Composable
internal fun ReleasesByLabelScreen(
    modifier: Modifier,
    labelId: String,
    searchText: String,
    snackbarHostState: SnackbarHostState,
    onReleaseClick: (String) -> Unit = {},
    onTitleUpdate: (title: String) -> Unit,
    viewModel: ReleasesByLabelViewModel,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ReleaseUiModel>
) {

    LaunchedEffect(key1 = labelId) {
        viewModel.updateLabelId(labelId)
        try {
            val label = viewModel.lookupLabel(labelId)
            onTitleUpdate(
                label.getNameWithDisambiguation()
            )
        } catch (e: Exception) {
            onTitleUpdate("[Label lookup failed]")
        }
    }

    viewModel.updateQuery(query = searchText)

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
