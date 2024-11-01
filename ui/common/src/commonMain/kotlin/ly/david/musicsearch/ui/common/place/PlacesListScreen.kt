package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun PlacesListScreen(
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { placeListItemModel: PlaceListItemModel? ->
        when (placeListItemModel) {
            is PlaceListItemModel -> {
                PlaceListItem(
                    place = placeListItemModel,
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
