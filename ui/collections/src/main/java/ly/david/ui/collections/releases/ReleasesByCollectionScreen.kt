package ly.david.ui.collections.releases

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.ui.commonlegacy.release.ReleasesListScreen
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ReleasesByCollectionScreen(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    showMoreInfo: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    viewModel: ReleasesByCollectionViewModel = koinViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val lazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
            .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        viewModel.setRemote(isRemote)
        viewModel.loadPagedEntities(collectionId)
    }

    LaunchedEffect(key1 = filterText) {
        viewModel.updateQuery(filterText)
    }

    ReleasesListScreen(
        lazyListState = lazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = lazyPagingItems,
        showMoreInfo = showMoreInfo,
        onReleaseClick = onReleaseClick,
        onDeleteFromCollection = onDeleteFromCollection,
    )
}
