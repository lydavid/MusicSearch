package ly.david.mbjc.ui.area.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.domain.AreaUiModel
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.relation.RelationCard

/**
 *
 * @param onAreaLookup Returns the [Area] from the relations lookup call.
 * This let our scaffold set its title if it hasn't already (which should only happen when arriving
 * through deeplink).
 */
@Composable
internal fun AreaRelationsScreen(
    modifier: Modifier = Modifier,
    areaId: String,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<UiModel>,
    onPagedRelationsChange: (Flow<PagingData<UiModel>>) -> Unit,
    onAreaLookup: (AreaUiModel) -> Unit,
    viewModel: AreaRelationsViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = areaId) {
        viewModel.fetchRelationsForResource(areaId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    LaunchedEffect(key1 = viewModel.area.value) {
        viewModel.area.value?.let {
            onAreaLookup(it)
        }
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
