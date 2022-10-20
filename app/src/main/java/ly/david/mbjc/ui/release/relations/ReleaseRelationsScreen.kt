package ly.david.mbjc.ui.release.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun ReleaseRelationsScreen(
    modifier: Modifier = Modifier,
    releaseId: String,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    viewModel: ReleaseRelationsViewModel = hiltViewModel(),
    onPagedRelationsChange: (Flow<PagingData<ly.david.data.domain.UiModel>>) -> Unit,
    lazyPagingItems: LazyPagingItems<ly.david.data.domain.UiModel>
) {

    LaunchedEffect(key1 = releaseId) {
        viewModel.loadRelations(releaseId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
    ) { uiModel: ly.david.data.domain.UiModel? ->

        when (uiModel) {
            is ly.david.data.domain.RelationUiModel -> {
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
