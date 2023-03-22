package ly.david.mbjc.ui.place.events

import androidx.compose.foundation.ExperimentalFoundationApi
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
import ly.david.data.domain.EventListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.event.EventListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EventsByPlaceScreen(
    placeId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    eventsLazyListState: LazyListState = rememberLazyListState(),
    eventsLazyPagingItems: LazyPagingItems<EventListItemModel>,
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

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = eventsLazyListState,
        lazyPagingItems = eventsLazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { eventListItemModel: EventListItemModel? ->
        when (eventListItemModel) {
            is EventListItemModel -> {
                EventListItem(
                    event = eventListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onEventClick(MusicBrainzResource.EVENT, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
