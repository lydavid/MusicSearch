package ly.david.musicsearch.shared.feature.details.releasegroup.releases

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.ui.commonlegacy.release.ReleasesListScreenLegacy
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ReleasesByReleaseGroupScreen(
    releaseGroupId: String,
    filterText: String,
    showMoreInfo: Boolean,
    snackbarHostState: SnackbarHostState,
    releasesLazyListState: LazyListState,
    releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onPagedReleasesFlowChange: (Flow<PagingData<ReleaseListItemModel>>) -> Unit = {},
    viewModel: ReleasesByReleaseGroupViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.loadPagedEntities(releaseGroupId)
        onPagedReleasesFlowChange(viewModel.pagedEntities)
    }
    viewModel.updateQuery(filterText)

    ReleasesListScreenLegacy(
        lazyListState = releasesLazyListState,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyPagingItems = releasesLazyPagingItems,
        showMoreInfo = showMoreInfo,
        onReleaseClick = onReleaseClick,
    )
}
