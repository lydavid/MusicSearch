package ly.david.ui.collections.places

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.commonlegacy.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.commonlegacy.place.PlaceListItem
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlacesByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: PlacesByCollectionViewModel = koinViewModel(),
) {
    val entity = MusicBrainzEntity.PLACE
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<PlaceListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedEntities(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: PlaceListItemModel? ->
        when (listItemModel) {
            is PlaceListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        PlaceListItem(
                            place = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onPlaceClick(entity, id, getNameWithDisambiguation())
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(listItemModel.id, listItemModel.name)
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
