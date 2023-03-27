package ly.david.mbjc.ui.collections.labels

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
import ly.david.data.domain.LabelListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.label.LabelListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LabelsByCollectionScreen(
    collectionId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<LabelListItemModel>,
    modifier: Modifier = Modifier,
    onLabelClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedLabelsFlowChange: (Flow<PagingData<LabelListItemModel>>) -> Unit = {},
    viewModel: LabelsByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.loadPagedResources(collectionId)
        onPagedLabelsFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: LabelListItemModel? ->
        when (listItemModel) {
            is LabelListItemModel -> {
                LabelListItem(
                    label = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onLabelClick(MusicBrainzResource.LABEL, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
