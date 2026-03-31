package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewNowPlayingHistoryListItem() {
    PreviewTheme {
        Surface {
            NowPlayingHistoryListItem(
                nowPlayingHistory = NowPlayingHistoryListItemModel(
                    id = "Immigrant Song by Led Zeppelin",
                    title = "Immigrant Song",
                    artist = "Led Zeppelin",
                    lastPlayed = Instant.parse("2023-09-23T11:42:20Z"),
                ),
                filterText = "ze",
            )
        }
    }
}
