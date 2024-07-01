package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheet() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        CollectionListItemModel(
                            id = "1",
                            isRemote = true,
                            name = "My remote CD collection",
                            entity = MusicBrainzEntity.RELEASE,
                        ),
                        CollectionListItemModel(
                            id = "2",
                            isRemote = false,
                            name = "My local CD collection",
                            entity = MusicBrainzEntity.RELEASE,
                        ),
                    ),
                ),
            )
            Column {
                CollectionBottomSheetContent(
                    collections = items.collectAsLazyPagingItems(),
                )
            }
        }
    }
}
