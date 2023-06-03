package ly.david.mbjc.ui.collections.labels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.LabelListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.label.LabelListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LabelsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onLabelClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: LabelsByCollectionViewModel = hiltViewModel(),
) {

    val entity = MusicBrainzResource.LABEL
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<LabelListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedResources)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: LabelListItemModel? ->
        when (listItemModel) {
            is LabelListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        LabelListItem(
                            label = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onLabelClick(entity, id, getNameWithDisambiguation())
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(listItemModel.id, listItemModel.name)
                    }
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
