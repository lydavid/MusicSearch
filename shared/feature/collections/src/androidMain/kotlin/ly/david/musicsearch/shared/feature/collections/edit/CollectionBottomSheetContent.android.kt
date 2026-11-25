package ly.david.musicsearch.shared.feature.collections.edit

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.collection.PendingEdit
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.CollectionContainsEntities
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState

private val items = MutableStateFlow(
    PagingData.from(
        listOf(
            CollectionListItemModel(
                id = "1",
                isRemote = true,
                name = "My remote CD collection",
                entity = MusicBrainzEntityType.RELEASE,
                visited = true,
                remoteEntityCount = 3,
                cachedEntityCount = 2,
                containsEntities = CollectionContainsEntities.None,
            ),
            CollectionListItemModel(
                id = "2",
                isRemote = false,
                name = "My local CD collection",
                entity = MusicBrainzEntityType.RELEASE,
                cachedEntityCount = 5,
                containsEntities = CollectionContainsEntities.All,
            ),
            CollectionListItemModel(
                id = "3",
                isRemote = false,
                name = "Another CD collection",
                entity = MusicBrainzEntityType.RELEASE,
                cachedEntityCount = 4,
                containsEntities = CollectionContainsEntities.Some,
            ),
        ) + (0..10).map {
            CollectionListItemModel(
                id = "${it + 4}",
                isRemote = false,
                name = "Another CD collection ${it + 1}",
                entity = MusicBrainzEntityType.RELEASE,
                cachedEntityCount = it,
                containsEntities = CollectionContainsEntities.None,
            )
        },
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContent() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentNoConfirm() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                    confirmChanges = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentWithQuery() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                    filterState = TopAppBarFilterState(
                        initialFilterText = "CD",
                        initialIsFilterMode = true,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentSyncing() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                    feedback = Feedback.Loading(
                        data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentLoading() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                    feedback = Feedback.Loading(
                        data = EditACollectionFeedback.Loading,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentApplyEnabled() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = items.collectAsLazyPagingItems(),
                    pendingEdits = mapOf(
                        "2" to CollectionListItemModel(
                            id = "2",
                            isRemote = false,
                            name = "My local CD collection",
                            entity = MusicBrainzEntityType.RELEASE,
                            cachedEntityCount = 5,
                            containsEntities = CollectionContainsEntities.All,
                            pendingEdit = PendingEdit.Remove,
                        ),
                        "4" to CollectionListItemModel(
                            id = "4",
                            isRemote = false,
                            name = "Another CD collection 1",
                            entity = MusicBrainzEntityType.RELEASE,
                            cachedEntityCount = 0,
                            containsEntities = CollectionContainsEntities.None,
                            pendingEdit = PendingEdit.Add,
                        ),
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentNoItems() {
    PreviewWithTransitionAndOverlays {
        Surface {
            CollectionBottomSheetContent(
                state = EditCollectionUiState(
                    defaultEntityType = MusicBrainzEntityType.RELEASE,
                    numberOfItemsToAddToCollection = 5,
                    lazyPagingItems = MutableStateFlow(
                        PagingData.from(
                            listOf<CollectionListItemModel>(),
                        ),
                    ).collectAsLazyPagingItems(),
                ),
            )
        }
    }
}
