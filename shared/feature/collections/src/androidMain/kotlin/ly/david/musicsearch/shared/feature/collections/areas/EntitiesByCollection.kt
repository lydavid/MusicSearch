package ly.david.musicsearch.shared.feature.collections.areas

import ReleaseListItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.artist.ArtistListItem
import ly.david.ui.common.event.EventListItem
import ly.david.ui.common.instrument.InstrumentListItem
import ly.david.ui.common.label.LabelListItem
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.place.PlaceListItem
import ly.david.ui.common.recording.RecordingListItem
import ly.david.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.ui.common.series.SeriesListItem
import ly.david.ui.common.work.WorkListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EntitiesByCollection(
    collectionId: String,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    isRemote: Boolean,
    filterText: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
//    viewModel: AreasByCollectionViewModel = koinViewModel(),
) {
    // TODO:
    val entity = MusicBrainzEntity.AREA
    val lazyListState = rememberLazyListState()
//    val lazyPagingItems: LazyPagingItems<AreaListItemModel> =
//        rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
//            .collectAsLazyPagingItems()

//    LaunchedEffect(key1 = collectionId) {
//        viewModel.setRemote(isRemote)
//        viewModel.loadPagedEntities(collectionId)
//    }
//
//    LaunchedEffect(key1 = filterText) {
//        viewModel.updateQuery(filterText)
//    }

    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is AreaListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        AreaListItem(
                            area = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
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
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
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
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is InstrumentListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        InstrumentListItem(
                            instrument = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is LabelListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        LabelListItem(
                            label = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is PlaceListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        PlaceListItem(
                            place = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is RecordingListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        RecordingListItem(
                            recording = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is ReleaseListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseListItem(
                            release = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
//                            showMoreInfo = showMoreInfo,
//                            requestForMissingCoverArtUrl = {
//                                requestForMissingCoverArtUrl(releaseListItemModel.id)
//                            },
                        ) {
                            onItemClick(
                                MusicBrainzEntity.RELEASE,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
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
                            modifier = Modifier.animateItemPlacement(),
                            requestForMissingCoverArtUrl = {
//                                try {
//                                    viewModel.getReleaseGroupCoverArtUrlFromNetwork(
//                                        releaseGroupId = listItemModel.id,
//                                    )
//                                } catch (ex: Exception) {
//                                    Timber.e(ex)
//                                }
                            },
                        ) {
                            onItemClick(
                                MusicBrainzEntity.RELEASE_GROUP,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is SeriesListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SeriesListItem(
                            series = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is WorkListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        WorkListItem(
                            work = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
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
