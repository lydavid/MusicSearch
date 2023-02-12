package ly.david.mbjc.ui.area.places

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.place.PlaceListItem

@Composable
internal fun PlacesByAreaScreen(
    areaId: String,
    filterText: String,
    placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    placesLazyListState: LazyListState = rememberLazyListState(),
    onPlaceClick: (destination: Destination, String, String) -> Unit = { _, _, _ -> },
    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit = {},
    viewModel: PlacesByAreaViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = areaId) {
        viewModel.loadPagedResources(areaId)
        onPagedPlacesFlowChange(viewModel.pagedResources)
    }
    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        lazyPagingItems = placesLazyPagingItems,
        modifier = modifier,
        lazyListState = placesLazyListState,
        snackbarHostState = snackbarHostState
    ) { placeListItemModel: PlaceListItemModel? ->
        when (placeListItemModel) {
            is PlaceListItemModel -> {
                PlaceListItem(place = placeListItemModel) {
                    onPlaceClick(Destination.LOOKUP_PLACE, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
