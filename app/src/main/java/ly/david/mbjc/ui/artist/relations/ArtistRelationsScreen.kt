package ly.david.mbjc.ui.artist.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun ArtistRelationsScreen(
    modifier: Modifier = Modifier,
    artistId: String,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<UiModel>,
    onPagedRelationsChange: (Flow<PagingData<UiModel>>) -> Unit,
    viewModel: ArtistRelationsViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = artistId) {
        viewModel.fetchRelationsForResource(artistId)
        onPagedRelationsChange(viewModel.pagedRelations)
    }

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
    ) { uiModel: UiModel? ->

        when (uiModel) {
            is RelationUiModel -> {
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
