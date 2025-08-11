package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSearchSpotifyBottomSheetContent() {
    PreviewTheme {
        Surface {
            Column {
                SearchSpotifyBottomSheetContent(
                    spotifyHistory = SpotifyHistoryListItemModel(
                        id = "spotify:track:2WfaOiMkCvy7F5fcp2zZ8L${Instant.parse("2024-05-01T00:16:57.029Z")}",
                        trackId = "spotify:track:2WfaOiMkCvy7F5fcp2zZ8L",
                        trackName = "Take on Me",
                        artistName = "a-ha",
                        albumName = "Hunting High and Low",
                        lastListened = Instant.parse("2024-05-01T00:16:57.029Z"),
                    ),
                )
            }
        }
    }
}
