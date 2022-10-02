package ly.david.mbjc.ui.release.tracks

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.domain.TrackUiModel
import ly.david.mbjc.data.network.WorkMusicBrainzModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.toDisplayTime
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun TrackCard(
    track: TrackUiModel,
//    showTrackArtists: Boolean = false,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
    onWorkClick: (WorkMusicBrainzModel) -> Unit = {},
    // no onTrackClick needed since Tracks exists in the context of a Release
) {

    // TODO: constraint
    ClickableListItem(
        onClick = { onRecordingClick(track.recordingId, track.title) },
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = track.number,
                modifier = Modifier.weight(1f),
                style = TextStyles.getCardBodyTextStyle(),
            )

            Column(
                modifier = Modifier
                    .weight(10f)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = track.title,
                    style = TextStyles.getCardTitleTextStyle(),

                    )
//                if (showTrackArtists) {
//                    Text(
//                        modifier = Modifier.padding(top = 4.dp),
//                        style = TextStyles.getCardBodyTextStyle(),
//                        text = "TODO"//uiTrack.artistCredits.getDisplayNames()
//                    )
//                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // TODO: constraint layout to keep start/end text on 1 line, wrapping only middle

            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodyTextStyle()
            )
        }

        // TODO: more content in new screen that expands, but maybe not cover the entire screen
    }
}

private val testTrack = TrackUiModel(
    id = "1",
    title = "Track title that is long and wraps",
    position = 1,
    number = "123",
    length = 25300000,
    mediumId = 1L,
    recordingId = "r1"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrackCardPreview() {
    PreviewTheme {
        Surface {
            TrackCard(
                track = testTrack,
//                showTrackArtists = true
            )
        }
    }
}
