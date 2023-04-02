package ly.david.mbjc.ui.collections.series

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
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.series.SeriesListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SeriesByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<SeriesListItemModel>,
    modifier: Modifier = Modifier,
    onSeriesClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedSeriesFlowChange: (Flow<PagingData<SeriesListItemModel>>) -> Unit = {},
    viewModel: SeriesByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
        onPagedSeriesFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: SeriesListItemModel? ->
        when (listItemModel) {
            is SeriesListItemModel -> {
                SeriesListItem(
                    series = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onSeriesClick(MusicBrainzResource.SERIES, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
