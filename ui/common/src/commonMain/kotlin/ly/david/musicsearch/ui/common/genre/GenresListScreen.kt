package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenresListScreen(
    lazyPagingItems: LazyPagingItems<GenreListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onGenreClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
    ) { listItemModel: GenreListItemModel? ->
        when (listItemModel) {
            is GenreListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        GenreListItem(
                            genre = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onGenreClick(
                                MusicBrainzEntity.GENRE,
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
