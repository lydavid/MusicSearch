package ly.david.musicsearch.ui.common.release

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun ReleasesListScreen(
    state: ReleasesListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    showMoreInfo: Boolean = true,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ReleaseListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseListItem(
                            release = listItemModel,
                            showMoreInfo = showMoreInfo,
                            requestForMissingCoverArtUrl = {
                                requestForMissingCoverArtUrl(listItemModel.id)
                            },
                        ) {
                            onItemClick(
                                MusicBrainzEntity.RELEASE,
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
