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
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.theme.TextStyles

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
                    id = "spotify:track:7ALurdGTM0BZMHhUcrM2AW${Instant.parse("2024-05-01T00:16:57.029Z")}",
                    trackId = "spotify:track:7ALurdGTM0BZMHhUcrM2AW",
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
