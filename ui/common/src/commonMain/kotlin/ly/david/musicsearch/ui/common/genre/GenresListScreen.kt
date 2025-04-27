package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenresListScreen(
    state: GenresListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is GenreListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        GenreListItem(
                            genre = listItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.GENRE,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }
            is LastUpdatedFooter -> {
                LastUpdatedFooterItem(
                    lastUpdated = listItemModel.lastUpdated,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
