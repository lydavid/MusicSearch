package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUi() {
    PreviewWithTransitionAndOverlays {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = true,
                        name = "Favorite works",
                        entity = MusicBrainzEntityType.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntityType.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        CollectionListUi(
            state = CollectionsListUiState(
                selectionState = rememberSelectionState(),
                lazyPagingItems = items.collectAsLazyPagingItems(),
            ),
            onSortClick = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUiSelection() {
    PreviewWithTransitionAndOverlays {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = true,
                        name = "Favorite works",
                        entity = MusicBrainzEntityType.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntityType.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        val selectionState = rememberSelectionState(totalCount = 300)
        selectionState.toggleSelection(id = "2", totalLoadedCount = 200)
        CollectionListUi(
            state = CollectionsListUiState(
                selectionState = selectionState,
                lazyPagingItems = items.collectAsLazyPagingItems(),
            ),
            onSortClick = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUiSelectedAll() {
    PreviewWithTransitionAndOverlays {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = false,
                        name = "Favorite works",
                        entity = MusicBrainzEntityType.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntityType.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        val selectionState = rememberSelectionState(totalCount = 2)
        selectionState.toggleSelectAll(ids = listOf("1", "2"))
        CollectionListUi(
            state = CollectionsListUiState(
                selectionState = selectionState,
                lazyPagingItems = items.collectAsLazyPagingItems(),
            ),
            onSortClick = {},
        )
    }
}
