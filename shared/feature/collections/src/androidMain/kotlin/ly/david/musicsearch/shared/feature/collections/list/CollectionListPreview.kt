package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCollectionList() {
    PreviewTheme {
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
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListEditMode() {
    PreviewTheme {
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
            topAppBarEditState = TopAppBarEditState(initialIsEditMode = true),
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}
