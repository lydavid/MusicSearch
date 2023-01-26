package ly.david.mbjc.ui.common.paging

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.GetReleaseCoverArtPath
import ly.david.data.persistence.release.ReleaseDao
import ly.david.mbjc.ui.release.ReleaseListItem

@HiltViewModel
internal class ReleasesListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    override val releaseDao: ReleaseDao
) : ViewModel(), GetReleaseCoverArtPath

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
                    showMoreInfo = showMoreInfo,
                    requestForMissingCoverArtPath = {
                        try {
                            viewModel.getReleaseCoverArtPathFromNetwork(releaseId = releaseListItemModel.id)
                        } catch (ex: Exception) {
                            // TODO: timber these
                            Log.d("Remove This", "ReleasesListScreen: $ex")
                            // Do nothing.
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
