package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSpotifyHistoryCard() {
    PreviewTheme {
        Surface {
            SpotifyHistoryCard(
                spotifyHistory = SpotifyHistoryListItemModel(
                    id = "spotify:track:7ALurdGTM0BZMHhUcrM2AW${Instant.parse("2024-05-01T00:16:57.029Z")}",
                    trackId = "spotify:track:7ALurdGTM0BZMHhUcrM2AW",
                    trackName = "シルエット",
                    artistName = "KANA-BOON",
                    albumName = "TIME",
                    trackLengthMilliseconds = 272425,
                    lastListened = Instant.parse("2024-05-01T00:16:57.029Z"),
                ),
            )
        }
    }
}
