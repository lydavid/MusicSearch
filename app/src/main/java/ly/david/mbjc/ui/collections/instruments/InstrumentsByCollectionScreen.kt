package ly.david.mbjc.ui.collections.instruments

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
import ly.david.data.domain.InstrumentListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.instrument.InstrumentListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun InstrumentsByCollectionScreen(
    collectionId: String,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<InstrumentListItemModel>,
    modifier: Modifier = Modifier,
    onInstrumentClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onPagedInstrumentsFlowChange: (Flow<PagingData<InstrumentListItemModel>>) -> Unit = {},
    viewModel: InstrumentsByCollectionViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = collectionId) {
        viewModel.loadPagedResources(collectionId)
        onPagedInstrumentsFlowChange(viewModel.pagedResources)
    }

    viewModel.updateQuery(filterText)

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: InstrumentListItemModel? ->
        when (listItemModel) {
            is InstrumentListItemModel -> {
                InstrumentListItem(
                    instrument = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onInstrumentClick(MusicBrainzResource.INSTRUMENT, id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
