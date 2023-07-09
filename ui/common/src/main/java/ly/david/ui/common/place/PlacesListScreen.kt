package ly.david.ui.common.place

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlacesListScreen(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
) {
    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState
    ) { placeListItemModel: PlaceListItemModel? ->
        when (placeListItemModel) {
            is PlaceListItemModel -> {
                PlaceListItem(
                    place = placeListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onPlaceClick(MusicBrainzEntity.PLACE, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
