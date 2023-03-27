package ly.david.mbjc.ui.collections.areas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.domain.AreaListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.AreaListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AreasByCollectionScreen(
    collectionId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<AreaListItemModel>,
    modifier: Modifier = Modifier,
    onAreaClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedAreasFlowChange: (Flow<PagingData<AreaListItemModel>>) -> Unit = {},
    viewModel: AreasByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.loadPagedResources(collectionId)
        onPagedAreasFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: AreaListItemModel? ->
        when (listItemModel) {
            is AreaListItemModel -> {
                AreaListItem(
                    area = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onAreaClick(MusicBrainzResource.AREA, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
