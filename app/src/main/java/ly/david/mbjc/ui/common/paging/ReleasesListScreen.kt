package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.ui.release.ReleaseCard

@Composable
internal fun ReleasesListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ReleaseUiModel>,
    onReleaseClick: (String) -> Unit = {},
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
