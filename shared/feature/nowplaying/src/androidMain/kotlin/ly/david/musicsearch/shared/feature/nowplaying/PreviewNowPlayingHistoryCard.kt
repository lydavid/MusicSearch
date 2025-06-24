package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewNowPlayingHistoryCard() {
    PreviewTheme {
        Surface {
            NowPlayingHistoryCard(
                nowPlayingHistory = NowPlayingHistoryListItemModel(
                    id = "Immigrant Song by Led Zeppelin",
                    title = "Immigrant Song",
                    artist = "Led Zeppelin",
                    lastPlayed = Instant.parse("2023-09-23T11:42:20Z"),
                ),
            )
        }
    }
}
