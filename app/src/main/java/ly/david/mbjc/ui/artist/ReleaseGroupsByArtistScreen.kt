package ly.david.mbjc.ui.artist

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
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    isSorted: Boolean,
    scaffoldState: ScaffoldState,
    onReleaseGroupClick: (String) -> Unit = {},
    onTitleUpdate: (title: String) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = artistId) {
        onTitleUpdate(viewModel.lookupArtist(artistId).getNameWithDisambiguation())
    }

    viewModel.updateQuery(query = searchText)
    viewModel.updateIsSorted(isSorted = isSorted)

    val lazyPagingItems: LazyPagingItems<UiData> = viewModel.pagedReleaseGroups.collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(lazyPagingItems) { uiData: UiData? ->
                when (uiData) {
                    is UiReleaseGroup -> {
                        ReleaseGroupCard(releaseGroup = uiData) {
                            onReleaseGroupClick(it.id)
                        }
                    }
                    is ListSeparator -> {
                        ListSeparatorHeader(text = uiData.text)
                    }
                    else -> {
                        // Do nothing.
                    }
                }
            }
        }
    }
}
