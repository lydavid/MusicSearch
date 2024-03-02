package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@PreviewScreenSizes
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
                    ),
                ),
            ),
        )

        Surface {
            CollectionList(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
