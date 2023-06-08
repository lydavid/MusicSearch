package ly.david.mbjc.ui.area.places

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.place.PlacesListScreen

@Composable
internal fun PlacesByAreaScreen(
    areaId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    placesLazyListState: LazyListState,
    placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit = {},
    viewModel: PlacesByAreaViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = areaId) {
        viewModel.loadPagedResources(areaId)
        onPagedPlacesFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PlacesListScreen(
        snackbarHostState = snackbarHostState,
        lazyListState = placesLazyListState,
        lazyPagingItems = placesLazyPagingItems,
        modifier = modifier,
        onPlaceClick = onPlaceClick
    )
}
