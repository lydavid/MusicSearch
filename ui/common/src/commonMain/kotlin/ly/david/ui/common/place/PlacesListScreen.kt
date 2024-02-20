package ly.david.ui.common.place

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlacesListScreen(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState,
    ) { placeListItemModel: PlaceListItemModel? ->
        when (placeListItemModel) {
            is PlaceListItemModel -> {
                PlaceListItem(
                    place = placeListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onPlaceClick(
                        MusicBrainzEntity.PLACE,
                        id,
                        getNameWithDisambiguation(),
                    )
                }
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
