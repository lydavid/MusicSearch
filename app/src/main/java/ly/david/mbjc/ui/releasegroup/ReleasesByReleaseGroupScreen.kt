package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.release.ReleaseCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    searchText: String,
    snackbarHostState: SnackbarHostState,
    onReleaseClick: (String) -> Unit = {},
    onTitleUpdate: (title: String, subtitle: String) -> Unit,
    viewModel: ReleasesByReleaseGroupViewModel = hiltViewModel()
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

    val lazyPagingItems: LazyPagingItems<ReleaseUiModel> = viewModel.pagedReleases.collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { releaseUiModel: ReleaseUiModel? ->
        when (releaseUiModel) {
            is ReleaseUiModel -> {
                ReleaseCard(releaseUiModel = releaseUiModel) {
                    onReleaseClick(it.id)
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
