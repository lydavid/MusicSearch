package ly.david.mbjc.ui.common.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.EventListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.event.EventListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EventsListScreen(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<EventListItemModel>,
    modifier: Modifier = Modifier,
    onEventClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
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
