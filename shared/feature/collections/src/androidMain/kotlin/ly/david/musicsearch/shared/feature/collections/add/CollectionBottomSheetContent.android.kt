package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.CollectionContainsEntities
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
                numberOfItemsToAddToCollection = 5,
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
                numberOfItemsToAddToCollection = 5,
                feedback = Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                ),
            )
        }
    }
}
