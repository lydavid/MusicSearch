package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val items = MutableStateFlow(
    PagingData.from(
        listOf(
            CollectionListItemModel(
                id = "1",
                isRemote = true,
                name = "My remote CD collection",
                entity = MusicBrainzEntityType.RELEASE,
                visited = true,
            ),
            CollectionListItemModel(
                id = "2",
                isRemote = false,
                name = "My local CD collection",
                entity = MusicBrainzEntityType.RELEASE,
            ),
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContent() {
    PreviewTheme {
        Surface {
            CollectionBottomSheetContent(
                collections = items.collectAsLazyPagingItems(),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionBottomSheetContentSyncing() {
    PreviewTheme {
        Surface {
            CollectionBottomSheetContent(
                collections = items.collectAsLazyPagingItems(),
                feedback = Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                ),
            )
        }
    }
}
