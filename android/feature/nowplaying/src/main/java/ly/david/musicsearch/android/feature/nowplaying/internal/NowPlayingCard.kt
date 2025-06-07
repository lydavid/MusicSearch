package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun NowPlayingHistoryCard(
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
