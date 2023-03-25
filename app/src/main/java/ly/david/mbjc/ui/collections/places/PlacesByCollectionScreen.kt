package ly.david.mbjc.ui.collections.places

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.screen.PlacesListScreen

@Composable
internal fun PlacesByCollectionScreen(
    collectionId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    modifier: Modifier = Modifier,
    onPlaceClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit = {},
    viewModel: PlacesByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.loadPagedResources(collectionId)
        onPagedPlacesFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PlacesListScreen(
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        onPlaceClick = onPlaceClick
    )
}
