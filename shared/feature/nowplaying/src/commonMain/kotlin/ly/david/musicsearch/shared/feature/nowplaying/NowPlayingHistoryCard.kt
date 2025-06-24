package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
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
