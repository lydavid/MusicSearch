package ly.david.mbjc.ui.label.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.RelationUiModel
import ly.david.data.domain.UiModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun LabelRelationsScreen(
    modifier: Modifier = Modifier,
    labelId: String,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    onPagedRelationsChange: (Flow<PagingData<UiModel>>) -> Unit,
    lazyPagingItems: LazyPagingItems<UiModel>,
    viewModel: LabelRelationsViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = labelId) {
        viewModel.loadRelations(labelId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
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
