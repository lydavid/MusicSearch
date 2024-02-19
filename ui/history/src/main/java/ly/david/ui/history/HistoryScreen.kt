package ly.david.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.commonlegacy.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HistoryScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryListItemModel) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is LookupHistoryListItemModel -> {
                HistoryListItem(
                    lookupHistory = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = onItemClick,
                    onDeleteItem = onDeleteItem,
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
