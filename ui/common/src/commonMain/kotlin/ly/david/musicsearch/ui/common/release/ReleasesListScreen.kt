package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun ReleasesListScreen(
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    isEditMode: Boolean = false,
    showMoreInfo: Boolean = true,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { releaseListItemModel: ReleaseListItemModel? ->
        when (releaseListItemModel) {
            is ReleaseListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseListItem(
                            release = releaseListItemModel,
                            showMoreInfo = showMoreInfo,
                            requestForMissingCoverArtUrl = {
                                requestForMissingCoverArtUrl(releaseListItemModel.id)
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
                            releaseListItemModel.id,
                            releaseListItemModel.name,
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
