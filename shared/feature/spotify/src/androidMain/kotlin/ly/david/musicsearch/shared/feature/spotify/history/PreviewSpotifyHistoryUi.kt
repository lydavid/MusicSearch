package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

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
                            text = Instant.parse("2024-05-01T02:29:38.973Z").getDateFormatted(),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:2ritsV4U3jq2LduJpovZ1A${Instant.parse("2024-05-01T02:29:38.973Z")}",
                            trackId = "spotify:track:2ritsV4U3jq2LduJpovZ1A",
                            trackName = "だれかの心臓になれたなら",
                            artistName = "Yurry Canon",
                            albumName = "Kardia",
                            trackLengthMilliseconds = 231724,
                            lastListened = Instant.parse("2024-05-01T02:29:38.973Z"),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:3hRRYgBeunE3PTmnzATTS0${Instant.parse("2024-05-01T02:29:38.973Z")}",
                            trackId = "spotify:track:3hRRYgBeunE3PTmnzATTS0",
                            trackName = "天体観測",
                            artistName = "BUMP OF CHICKEN",
                            albumName = "jupiter",
                            lastListened = Instant.parse("2024-05-01T02:29:38.973Z"),
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = Instant.parse("2024-04-30T02:29:38.973Z").getDateFormatted(),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:2ritsV4U3jq2LduJpovZ1A${Instant.parse("2024-04-30T02:29:38.973Z")}",
                            trackId = "spotify:track:2ritsV4U3jq2LduJpovZ1A",
                            trackName = "だれかの心臓になれたなら",
                            artistName = "Yurry Canon",
                            albumName = "Kardia",
                            trackLengthMilliseconds = 231724,
                            lastListened = Instant.parse("2024-04-30T02:29:38.973Z"),
                        ),
                    ),
                ),
            )
            SpotifyHistoryUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
