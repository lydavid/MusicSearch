package ly.david.ui.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.history.internal.HistoryListItem

@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryReleaseGroup(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 9999,
                    imageUrl = imageUrl,
                    lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
                ),
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryRelease(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    entity = MusicBrainzEntity.RELEASE,
                    id = "165f6643-2edb-4795-9abe-26bd0533e59d",
                    imageUrl = imageUrl,
                    lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
                ),
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryArtist(
    imageUrl: String = "https://www.example.com/image.jpg",
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "月詠み",
                    entity = MusicBrainzEntity.ARTIST,
                    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                    imageUrl = imageUrl,
                    lastAccessed = Instant.parse("2023-05-02T00:00:00Z"),
                ),
            )
        }
    }
}
