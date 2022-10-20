package ly.david.mbjc.ui.area.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.RelationUiModel
import ly.david.data.domain.UiModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun AreaRelationsScreen(
    modifier: Modifier = Modifier,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<UiModel>,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->

        when (uiModel) {
            is RelationUiModel -> {
                RelationCard(
                    relation = uiModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
