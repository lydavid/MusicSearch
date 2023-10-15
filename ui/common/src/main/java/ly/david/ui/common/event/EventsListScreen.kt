package ly.david.ui.common.event

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.EventListItemModel
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
        snackbarHostState = snackbarHostState,
    ) { eventListItemModel: EventListItemModel? ->
        when (eventListItemModel) {
            is EventListItemModel -> {
                EventListItem(
                    event = eventListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onEventClick(ly.david.musicsearch.core.models.network.MusicBrainzEntity.EVENT, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
