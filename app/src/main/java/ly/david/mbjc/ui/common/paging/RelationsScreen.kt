package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.RelationListItemModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.relation.RelationListItem

@Composable
internal fun RelationsScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
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
