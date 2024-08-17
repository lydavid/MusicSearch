package ly.david.musicsearch.ui.common.artist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsListScreen(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ArtistListItemModel>,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: ArtistListItemModel? ->
        when (listItemModel) {
            is ArtistListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ArtistListItem(
                            artist = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                MusicBrainzEntity.ARTIST,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = onDeleteFromCollection == null,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
