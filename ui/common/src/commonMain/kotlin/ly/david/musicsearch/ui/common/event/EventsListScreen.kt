package ly.david.musicsearch.ui.common.event

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun EventsListScreen(
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<EventListItemModel>,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { eventListItemModel: EventListItemModel? ->
        when (eventListItemModel) {
            is EventListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        EventListItem(
                            event = eventListItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.EVENT,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            eventListItemModel.id,
                            eventListItemModel.name,
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
