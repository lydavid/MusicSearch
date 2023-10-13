package ly.david.ui.collections.releasegroups

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ReleaseGroupsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onReleaseGroupClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: ReleaseGroupsByCollectionViewModel = koinViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedEntities(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    LaunchedEffect(key1 = isSorted) {
        viewModel.updateSorted(sorted = isSorted)
    }

    ReleaseGroupsListScreen(
        lazyListState = lazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = lazyPagingItems,
        onReleaseGroupClick = onReleaseGroupClick,
        onDeleteFromCollection = onDeleteFromCollection
    )
}
