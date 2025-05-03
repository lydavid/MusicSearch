package ly.david.musicsearch.ui.common.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.artist.ArtistListItem
import ly.david.musicsearch.ui.common.event.EventListItem
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupListItem

@Composable
fun EntitiesListScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    now: Instant = Clock.System.now(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is AreaListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        AreaListItem(
                            area = listItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.AREA,
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
                        onDeleteFromCollection?.invoke(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is EventListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        EventListItem(
                            event = listItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.EVENT,
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

            is ReleaseGroupListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseGroupListItem(
                            releaseGroup = listItemModel,
                            showType = false,
                            requestForMissingCoverArtUrl = {
                                requestForMissingCoverArtUrl(listItemModel.id)
                            },
                        ) {
                            onItemClick(
                                MusicBrainzEntity.RELEASE_GROUP,
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

            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is LastUpdatedFooter -> {
                LastUpdatedFooterItem(
                    lastUpdated = listItemModel.lastUpdated,
                    now = now,
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
