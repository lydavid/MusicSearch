package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewNowPlayingHistoryUi() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "separator1",
                            text = "Saturday, July 7, 2023",
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "1",
                            title = "Title",
                            artist = "Artist",
                            lastPlayed = Instant.parse("2023-07-15T11:42:20Z"),
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "2",
                            title = "Another Title",
                            artist = "A different artist",
                            lastPlayed = Instant.parse("2023-07-15T11:42:19Z"),
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = "Friday, July 6, 2023",
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "3",
                            title = "Yet Another Title",
                            artist = "A different artist",
                            lastPlayed = Instant.parse("2023-07-15T11:42:19Z"),
                        ),
                    ),
                ),
            )
            NowPlayingHistoryUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
// endregion
