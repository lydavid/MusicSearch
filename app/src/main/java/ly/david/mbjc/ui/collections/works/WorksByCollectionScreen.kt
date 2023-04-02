package ly.david.mbjc.ui.collections.works

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
import ly.david.data.domain.WorkListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.work.WorkListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WorksByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<WorkListItemModel>,
    modifier: Modifier = Modifier,
    onWorkClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedWorksFlowChange: (Flow<PagingData<WorkListItemModel>>) -> Unit = {},
    viewModel: WorksByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
        onPagedWorksFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: WorkListItemModel? ->
        when (listItemModel) {
            is WorkListItemModel -> {
                WorkListItem(
                    work = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onWorkClick(MusicBrainzResource.WORK, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
