package ly.david.musicsearch.android.feature.spotify.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.common.getTimeFormatted
import ly.david.musicsearch.core.models.common.toDisplayTime
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun SpotifyHistoryCard(
    spotifyHistory: SpotifyHistoryListItemModel,
    modifier: Modifier = Modifier,
    onClick: SpotifyHistoryListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = spotifyHistory.trackName.orEmpty(),
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            Column {
                Text(
                    text = spotifyHistory.albumName.orEmpty(),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
                Text(
                    text = spotifyHistory.artistName.orEmpty(),
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
                    text = spotifyHistory.numberOfListens.toString(),
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

@PreviewLightDark
@Composable
internal fun PreviewSpotifyHistoryCard() {
    PreviewTheme {
        Surface {
            SpotifyHistoryCard(
                spotifyHistory = SpotifyHistoryListItemModel(
                    id = "1di1C0QI6Y92yZPYn6XYAZ",
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
