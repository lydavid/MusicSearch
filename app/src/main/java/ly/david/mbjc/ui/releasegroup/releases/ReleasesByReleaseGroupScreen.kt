package ly.david.mbjc.ui.releasegroup.releases

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

@Composable
internal fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    searchText: String,
    snackbarHostState: SnackbarHostState,
    onReleaseClick: (String) -> Unit = {},
    onTitleUpdate: (title: String, subtitle: String) -> Unit,
    viewModel: ReleasesByReleaseGroupViewModel,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ReleaseUiModel>
) {

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.updateReleaseGroupId(releaseGroupId)
        try {
            val releaseGroup = viewModel.lookupReleaseGroup(releaseGroupId)
            onTitleUpdate(
                releaseGroup.getNameWithDisambiguation(),
                "Release Group by ${releaseGroup.artistCredits}"
            )
        } catch (e: Exception) {
            onTitleUpdate("[Release group lookup failed]", "[error]")
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
