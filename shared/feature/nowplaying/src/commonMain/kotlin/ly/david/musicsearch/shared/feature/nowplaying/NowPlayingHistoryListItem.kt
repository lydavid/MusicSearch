package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun NowPlayingHistoryListItem(
    nowPlayingHistory: NowPlayingHistoryListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
    onClick: NowPlayingHistoryListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            HighlightableText(
                text = nowPlayingHistory.title,
                highlightedText = filterText,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            HighlightableText(
                text = nowPlayingHistory.artist,
                highlightedText = filterText,
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
