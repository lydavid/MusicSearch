package ly.david.ui.common.event

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.EventListItemModel
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsListScreen(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<EventListItemModel>,
    modifier: Modifier = Modifier,
    onEventClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
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
                    onEventClick(MusicBrainzEntity.EVENT, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
