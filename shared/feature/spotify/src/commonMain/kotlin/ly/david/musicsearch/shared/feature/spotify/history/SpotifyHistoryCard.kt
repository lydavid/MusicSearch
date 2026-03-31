package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun SpotifyHistoryListItem(
    spotifyHistory: SpotifyHistoryListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
    onClick: SpotifyHistoryListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            HighlightableText(
                text = spotifyHistory.trackName.orEmpty(),
                highlightedText = filterText,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            Column {
                HighlightableText(
                    text = spotifyHistory.albumName.orEmpty(),
                    highlightedText = filterText,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
                HighlightableText(
                    text = spotifyHistory.artistName.orEmpty(),
                    highlightedText = filterText,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = spotifyHistory.lastListened.getTimeFormatted(),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
                Text(
                    text = spotifyHistory.trackLengthMilliseconds.toDisplayTime(),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        },
        modifier = modifier.clickable { onClick(spotifyHistory) },
    )
}
