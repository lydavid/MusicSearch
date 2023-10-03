package ly.david.mbjc.ui.artist.releasegroups

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.ui.common.releasegroup.ReleaseGroupsListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ReleaseGroupsByArtistScreen(
    artistId: String,
    modifier: Modifier,
    filterText: String,
    isSorted: Boolean,
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onReleaseGroupClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onPagedReleaseGroupsChange: (Flow<PagingData<ListItemModel>>) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.loadPagedEntities(artistId)
        onPagedReleaseGroupsChange(viewModel.pagedEntities)
    }

    viewModel.updateQuery(query = filterText)
    viewModel.updateSorted(sorted = isSorted)

    ReleaseGroupsListScreen(
        lazyListState = lazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = lazyPagingItems,
        onReleaseGroupClick = onReleaseGroupClick
    )
}
