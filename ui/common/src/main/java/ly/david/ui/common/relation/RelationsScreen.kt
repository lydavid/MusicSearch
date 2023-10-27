package ly.david.ui.common.relation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler

@Composable
fun RelationsListScreen(
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
        snackbarHostState = snackbarHostState,
    ) { listItemModel: ListItemModel? ->

        when (listItemModel) {
            is RelationListItemModel -> {
                RelationListItem(
                    relation = listItemModel,
                    onItemClick = { entity, id, title ->
                        if (entity == MusicBrainzEntity.URL) error("URLs should not be handled in this screen.")
                        onItemClick(entity, id, title)
                    },
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
