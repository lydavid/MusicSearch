package ly.david.mbjc.ui.collections.series

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.series.SeriesListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SeriesByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onSeriesClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: SeriesByCollectionViewModel = hiltViewModel(),
) {

    val entity = MusicBrainzResource.SERIES
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<SeriesListItemModel> =
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
    ) { listItemModel: SeriesListItemModel? ->
        when (listItemModel) {
            is SeriesListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SeriesListItem(
                            series = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onSeriesClick(entity, id, getNameWithDisambiguation())
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
