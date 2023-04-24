package ly.david.mbjc.ui.collections.artists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.artist.ArtistListItem
import ly.david.mbjc.ui.common.listitem.SwipeToDeleteListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ArtistsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
//    lazyListState: LazyListState,
//    lazyPagingItems: LazyPagingItems<ArtistListItemModel>,
    modifier: Modifier = Modifier,
    onArtistClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
//    onPagedArtistsFlowChange: (Flow<PagingData<ArtistListItemModel>>) -> Unit = {},
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
    viewModel: ArtistsByCollectionViewModel = hiltViewModel(),
) {

    val artistsLazyListState = rememberLazyListState()
//    var pagedArtistsFlow: Flow<PagingData<ArtistListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val artistsLazyPagingItems: LazyPagingItems<ArtistListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedResources)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
//        onPagedArtistsFlowChange(viewModel.pagedResources)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = artistsLazyListState,
        lazyPagingItems = artistsLazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ArtistListItemModel? ->

        when (listItemModel) {
            is ArtistListItemModel -> {
                SwipeToDeleteListItem(
                    dismissContent = {
                        ArtistListItem(
                            artist = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onArtistClick(MusicBrainzResource.ARTIST, id, getNameWithDisambiguation())
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(listItemModel.id, listItemModel.name)
                    }
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
