package ly.david.musicsearch.ui.common.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun EventsListScreen(
    state: EventsListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
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
