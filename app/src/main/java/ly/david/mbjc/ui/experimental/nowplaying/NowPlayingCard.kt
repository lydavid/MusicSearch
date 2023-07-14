package ly.david.mbjc.ui.experimental.nowplaying

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ly.david.data.common.toDisplayDate
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun NowPlayingCard(
    nowPlayingHistory: NowPlayingHistoryListItemModel,
) {
    Card {
        Column {
            Text(text = nowPlayingHistory.id)
            Text(text = nowPlayingHistory.text)
            Text(text = nowPlayingHistory.title)
            Text(text = nowPlayingHistory.lastPlayed.toDisplayDate())
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewNowPlayingCard() {
    PreviewTheme {
        Surface {
            NowPlayingCard(
                nowPlayingHistory = NowPlayingHistoryListItemModel(
                    id = "a_b",
                    title = "title",
                    text = "text"
                )
            )
        }
    }
}
