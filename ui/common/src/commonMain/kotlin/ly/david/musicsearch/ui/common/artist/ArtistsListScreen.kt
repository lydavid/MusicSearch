package ly.david.musicsearch.ui.common.artist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun ArtistsListScreen(
    state: ArtistsListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ArtistListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ArtistListItem(
                            artist = listItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.ARTIST,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
                    onDelete = {
                        onDeleteFromCollection(
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
