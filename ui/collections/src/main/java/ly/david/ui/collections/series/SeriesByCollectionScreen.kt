package ly.david.ui.collections.series

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.series.SeriesListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SeriesByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onSeriesClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: SeriesByCollectionViewModel = koinViewModel(),
) {
    val entity = MusicBrainzEntity.SERIES
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<SeriesListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedEntities(collectionId)
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
