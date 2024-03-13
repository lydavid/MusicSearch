package ly.david.ui.commonlegacy.releasegroup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.releasegroup.ReleaseGroupsListScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ReleaseGroupsListScreenLegacy(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    onReleaseGroupClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    viewModel: ReleaseGroupsListViewModel = koinViewModel(),
) {
    ReleaseGroupsListScreen(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        onReleaseGroupClick = onReleaseGroupClick,
        onDeleteFromCollection = onDeleteFromCollection,
        requestForMissingCoverArtUrl = {
            try {
                viewModel.getReleaseGroupCoverArtUrlFromNetwork(
                    releaseGroupId = it,
                )
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    )
}
