package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ui.release.ReleaseListItem

@Composable
internal fun ReleasesListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    showMoreInfo: Boolean = true,
    onReleaseClick: (String, String) -> Unit,
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
                    showMoreInfo = showMoreInfo
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

// TODO: Previewing compose paging currently not supported: https://issuetracker.google.com/issues/194544557#comment18
//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun Preview() {
//    PreviewTheme {
//        ReleasesListScreen(
//            lazyPagingItems = flowOf(PagingData.from(listOf(
//                ReleaseUiModel(
//                    id = "1",
//                    name = "Some Release",
//                    disambiguation = "That one"
//                )
//            ))).collectAsLazyPagingItems()
//        )
//    }
//}
