package ly.david.mbjc.ui.artist.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun ArtistRelationsScreen(
    modifier: Modifier = Modifier,
    artistId: String,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ly.david.data.domain.UiModel>,
    onPagedRelationsChange: (Flow<PagingData<ly.david.data.domain.UiModel>>) -> Unit,
    viewModel: ArtistRelationsViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = artistId) {
        viewModel.loadRelations(artistId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
    ) { uiModel: ly.david.data.domain.UiModel? ->

        when (uiModel) {
            is ly.david.data.domain.RelationUiModel -> {
                RelationCard(
                    relation = uiModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
