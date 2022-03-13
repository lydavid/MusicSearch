package ly.david.mbjc.ui.artist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    state: LazyListState,
    scaffoldState: ScaffoldState,
    onReleaseGroupClick: (String) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    viewModel.updateArtist(artistId = artistId)
    viewModel.updateQuery(query = searchText)

    val lazyPagingItems: LazyPagingItems<UiReleaseGroup> = viewModel.pagedReleaseGroups.collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            state = state,
            modifier = modifier
        ) {
            items(lazyPagingItems) { releaseGroup: UiReleaseGroup? ->
                if (releaseGroup == null) return@items
                ReleaseGroupCard(releaseGroup = releaseGroup) {
                    onReleaseGroupClick(it.id)
                }
            }
        }
    }

//                    uiReleaseGroups.sortAndGroupByTypes().forEach { (type, releaseGroupsForType) ->
//
//                        // TODO: clicking on header should collapse the group
//                        stickyHeader {
//                            StickyHeader(text = "$type (${releaseGroupsForType.size})")
//                        }
//                        items(releaseGroupsForType.sortedBy {
//                            it.firstReleaseDate.toDate()
//                        }) { releaseGroup ->
//                            ReleaseGroupCard(releaseGroup = releaseGroup) {
//                                onReleaseGroupClick(it.id)
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
}
