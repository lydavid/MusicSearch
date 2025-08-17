package ly.david.musicsearch.shared.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.history.internal.HistoryListItem
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewLookupHistoryReleaseGroup(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewWithSharedElementTransition {
        HistoryListItem(
            LookupHistoryListItemModel(
                title = "欠けた心象、世のよすが",
                entity = MusicBrainzEntityType.RELEASE_GROUP,
                id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                numberOfVisits = 9999,
                imageUrl = imageUrl,
                lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupHistoryRelease(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewWithSharedElementTransition {
        HistoryListItem(
            LookupHistoryListItemModel(
                title = "欠けた心象、世のよすが",
                entity = MusicBrainzEntityType.RELEASE,
                id = "165f6643-2edb-4795-9abe-26bd0533e59d",
                imageUrl = imageUrl,
                lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupHistoryArtist(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewWithSharedElementTransition {
        HistoryListItem(
            LookupHistoryListItemModel(
                title = "月詠み",
                entity = MusicBrainzEntityType.ARTIST,
                id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                imageUrl = imageUrl,
                lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
            ),
        )
    }
}
