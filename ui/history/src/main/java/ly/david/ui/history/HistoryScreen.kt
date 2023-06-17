package ly.david.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.history.LookupHistoryRoomModel
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HistoryScreen(
    lazyPagingItems: LazyPagingItems<LookupHistoryRoomModel>,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryRoomModel) -> Unit = {}
) {

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
    ) { lookupHistory: LookupHistoryRoomModel? ->
        when (lookupHistory) {
            is LookupHistoryRoomModel -> {
                HistoryListItem(
                    lookupHistory = lookupHistory,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = onItemClick,
                    onDeleteItem = onDeleteItem
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
