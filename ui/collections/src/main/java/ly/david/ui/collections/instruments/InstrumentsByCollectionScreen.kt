package ly.david.ui.collections.instruments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.instrument.InstrumentListItem
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun InstrumentsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onInstrumentClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: InstrumentsByCollectionViewModel = hiltViewModel(),
) {
    val entity = MusicBrainzEntity.INSTRUMENT
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<InstrumentListItemModel> =
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
    ) { listItemModel: InstrumentListItemModel? ->
        when (listItemModel) {
            is InstrumentListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        InstrumentListItem(
                            instrument = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onInstrumentClick(entity, id, getNameWithDisambiguation())
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
