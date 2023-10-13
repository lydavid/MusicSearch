package ly.david.ui.common.relation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@Composable
fun RelationsScreen(
    lazyPagingItems: LazyPagingItems<RelationListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ListItemModel? ->

        when (listItemModel) {
            is RelationListItemModel -> {
                RelationListItem(
                    relation = listItemModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
