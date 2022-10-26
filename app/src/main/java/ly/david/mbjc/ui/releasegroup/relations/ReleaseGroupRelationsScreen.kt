package ly.david.mbjc.ui.releasegroup.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.UiModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun ReleaseGroupRelationsScreen(
    modifier: Modifier = Modifier,
    releaseGroupId: String,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    viewModel: ReleaseGroupRelationsViewModel = hiltViewModel(),
    onPagedRelationsChange: (Flow<PagingData<UiModel>>) -> Unit,
    lazyPagingItems: LazyPagingItems<UiModel>
) {

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.loadRelations(releaseGroupId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
    ) { uiModel: UiModel? ->

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
