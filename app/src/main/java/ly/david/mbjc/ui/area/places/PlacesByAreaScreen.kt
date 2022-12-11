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
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    placesLazyListState: LazyListState = rememberLazyListState(),
    placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    onPlaceClick: (destination: Destination, String, String) -> Unit,
    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit,
    filterText: String,
    viewModel: PlacesByAreaViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = areaId) {
        viewModel.loadPagedResources(areaId)
        onPagedPlacesFlowChange(viewModel.pagedResources)
    }
    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = placesLazyListState,
        lazyPagingItems = placesLazyPagingItems,
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
