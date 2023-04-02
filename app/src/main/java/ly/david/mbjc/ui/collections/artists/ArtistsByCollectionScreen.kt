package ly.david.mbjc.ui.collections.artists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.artist.ArtistListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ArtistsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ArtistListItemModel>,
    modifier: Modifier = Modifier,
    onArtistClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedArtistsFlowChange: (Flow<PagingData<ArtistListItemModel>>) -> Unit = {},
    viewModel: ArtistsByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
        onPagedArtistsFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ArtistListItemModel? ->
        when (listItemModel) {
            is ArtistListItemModel -> {
                ArtistListItem(
                    artist = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onArtistClick(MusicBrainzResource.ARTIST, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
