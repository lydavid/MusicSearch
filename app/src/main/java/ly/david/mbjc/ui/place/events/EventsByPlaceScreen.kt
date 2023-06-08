package ly.david.mbjc.ui.place.events

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
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.event.EventsListScreen

@Composable
internal fun EventsByPlaceScreen(
    placeId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<EventListItemModel>,
    onEventClick: (entity: MusicBrainzResource, String, String) -> Unit,
    onPagedEventsFlowChange: (Flow<PagingData<EventListItemModel>>) -> Unit,
    filterText: String,
    viewModel: EventsByPlaceViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = placeId) {
        viewModel.loadPagedResources(placeId)
        onPagedEventsFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    EventsListScreen(
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        onEventClick = onEventClick
    )
}
