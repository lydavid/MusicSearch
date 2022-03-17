package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ly.david.mbjc.data.domain.UiRelease
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.release.ReleaseCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    searchText: String,
    scaffoldState: ScaffoldState,
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

    val lazyPagingItems: LazyPagingItems<UiRelease> = viewModel.pagedReleases.collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(lazyPagingItems) { uiRelease: UiRelease? ->
                when (uiRelease) {
                    is UiRelease -> {
                        ReleaseCard(uiRelease = uiRelease) {
                            onReleaseClick(it.id)
                        }
                    }
                    else -> {
                        // Do nothing.
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ReleasesByReleaseGroupScreen(
//    modifier: Modifier,
//    releaseGroupId: String,
//    onReleaseClick: (String) -> Unit = {},
//    viewModel: OldReleasesByReleaseGroupViewModel = hiltViewModel()
//) {
//    val uiState by produceState(initialValue = UiState(isLoading = true)) {
//        value = UiState(response = viewModel.getReleasesByReleaseGroup(releaseGroupId))
//    }
//
//    when {
//        uiState.response != null -> {
//            uiState.response?.let { releases ->
//
//                LazyColumn(
//                    modifier = modifier
//                ) {
//                    val grouped = releases.groupBy { it.status ?: "(No status)" }
//                    grouped.forEach { (status, releasesWithStatus) ->
//                        stickyHeader {
//                            ListSeparatorHeader(text = status)
//                        }
//                        items(releasesWithStatus.sortedBy {
//                            it.date?.toDate()
//                        }) { release ->
//                            // TODO: sort by date ascending
//                            ReleaseCard(uiRelease = release) {
//                                onReleaseClick(it.id)
//                            }
//                        }
//                    }
//
//                }
//            }
//
//        }
//        uiState.isLoading -> {
//            FullScreenLoadingIndicator()
//        }
//        else -> {
//            Text(text = "error...")
//        }
//    }
//}
