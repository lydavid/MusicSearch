package ly.david.musicsearch.shared.feature.collections.works

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import ly.david.ui.common.work.WorkListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WorksByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onWorkClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: WorksByCollectionViewModel = koinViewModel(),
) {
    val entity = MusicBrainzEntity.WORK
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<WorkListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedEntities(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: WorkListItemModel? ->
        when (listItemModel) {
            is WorkListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        WorkListItem(
                            work = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onWorkClick(entity, id, getNameWithDisambiguation())
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(listItemModel.id, listItemModel.name)
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
