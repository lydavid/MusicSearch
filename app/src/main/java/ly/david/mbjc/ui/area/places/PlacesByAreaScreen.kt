package ly.david.mbjc.ui.area.places

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.ui.common.place.PlacesListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlacesByAreaScreen(
    areaId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    placesLazyListState: LazyListState,
    placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit = {},
    viewModel: PlacesByAreaViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = areaId) {
        viewModel.loadPagedEntities(areaId)
        onPagedPlacesFlowChange(viewModel.pagedEntities)
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
