package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUi() {
    PreviewWithSharedElementTransition {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = true,
                        name = "Favorite works",
                        entity = MusicBrainzEntity.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntity.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        CollectionListUi(
            selectionState = rememberSelectionState(),
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUiSelection() {
    PreviewWithSharedElementTransition {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = true,
                        name = "Favorite works",
                        entity = MusicBrainzEntity.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntity.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        val selectionState = rememberSelectionState(totalCount = 300)
        selectionState.toggleSelection(id = "2", totalLoadedCount = 200)
        CollectionListUi(
            selectionState = selectionState,
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListUiSelectedAll() {
    PreviewWithSharedElementTransition {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = false,
                        name = "Favorite works",
                        entity = MusicBrainzEntity.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntity.RELEASE,
                        visited = true,
                    ),
                ),
            ),
        )

        val selectionState = rememberSelectionState(totalCount = 2)
        selectionState.toggleSelectAll(ids = listOf("1", "2"))
        CollectionListUi(
            selectionState = selectionState,
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}
