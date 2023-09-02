package ly.david.ui.nowplaying

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.core.common.getTimeFormatted
import ly.david.data.core.common.toDate
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun NowPlayingCard(
    nowPlayingHistory: NowPlayingHistoryListItemModel,
    modifier: Modifier = Modifier,
    onClick: NowPlayingHistoryListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = nowPlayingHistory.title,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            Text(
                text = nowPlayingHistory.artist,
                style = TextStyles.getCardBodySubTextStyle(),
            )
        },
        trailingContent = {
            Text(
                text = nowPlayingHistory.lastPlayed.getTimeFormatted(),
                style = TextStyles.getCardBodySubTextStyle(),
            )
        },
        modifier = modifier.clickable { onClick(nowPlayingHistory) },
    )
}

@DefaultPreviews
@Composable
internal fun PreviewNowPlayingCard() {
    PreviewTheme {
        Surface {
            NowPlayingCard(
                nowPlayingHistory = NowPlayingHistoryListItemModel(
                    id = "Immigrant Song by Led Zeppelin",
                    title = "Immigrant Song",
                    artist = "Led Zeppelin",
                    lastPlayed = "2023-07-15 11:42:20".toDate(),
                )
            )
        }
    }
}
