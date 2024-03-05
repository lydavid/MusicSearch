package ly.david.musicsearch.shared.feature.collections.single

import ReleaseListItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.artist.ArtistListItem
import ly.david.ui.common.event.EventListItem
import ly.david.ui.common.fullscreen.FullScreenText
import ly.david.ui.common.instrument.InstrumentListItem
import ly.david.ui.common.label.LabelListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.place.PlaceListItem
import ly.david.ui.common.recording.RecordingListItem
import ly.david.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.ui.common.series.SeriesListItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.work.WorkListItem

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
//    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
) {
    val collection = state.collection
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = collection?.name.orEmpty(),
                scrollBehavior = scrollBehavior,
                showFilterIcon = true,
                overflowDropdownMenuItems = {
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            entity = MusicBrainzEntity.COLLECTION,
                            entityId = collection.id,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (collection?.entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.sortReleaseGroupListItems,
                            onToggle = {
                                eventSink(CollectionUiEvent.UpdateSortReleaseGroupListItems(it))
                            },
                        )
                    }
                    if (collection?.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.showMoreInfoInReleaseListItem,
                            onToggle = {
                                eventSink(CollectionUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                },
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(CollectionUiEvent.UpdateQuery(it))
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding),
            )
        } else {
            CollectionPlatformContent(
                lazyPagingItems = state.lazyPagingItems,
                entity = collection.entity,
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
                showMoreInfoInReleaseListItem = state.showMoreInfoInReleaseListItem,
                onItemClick = { entity, id, title ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entity,
                            id,
                            title,
                        ),
                    )
                },
//                onDeleteFromCollection = onDeleteFromCollection,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionPlatformContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    entity: MusicBrainzEntity,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    showMoreInfoInReleaseListItem: Boolean = true,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
) {
    EntitiesByCollection(
        lazyPagingItems = lazyPagingItems,
        entity = entity,
        snackbarHostState = snackbarHostState,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
        onDeleteFromCollection = onDeleteFromCollection,
        onItemClick = onItemClick,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EntitiesByCollection(
    lazyPagingItems: androidx.paging.compose.LazyPagingItems<ListItemModel>,
    entity: MusicBrainzEntity,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    showMoreInfoInReleaseListItem: Boolean = true,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
) {
    val lazyListState = rememberLazyListState()

    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

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
                            showMoreInfo = showMoreInfoInReleaseListItem,
//                            requestForMissingCoverArtUrl = {
//                                requestForMissingCoverArtUrl(releaseListItemModel.id)
//                            },
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
