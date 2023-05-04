package ly.david.mbjc.ui.collections.releasegroups

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.screen.ReleaseGroupsListScreen

@Composable
internal fun ReleaseGroupsByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onReleaseGroupClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: ReleaseGroupsByCollectionViewModel = hiltViewModel()
) {

    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedResources)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedResources(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    LaunchedEffect(key1 = isSorted) {
        viewModel.updateSorted(sorted = isSorted)
    }

    ReleaseGroupsListScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        onReleaseGroupClick = onReleaseGroupClick,
        onDeleteFromCollection = onDeleteFromCollection
    )
}
