package ly.david.mbjc.ui.release.tracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.TrackUiModel
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.recording.RecordingCard
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

/**
 * Also see [RecordingCard].
 */
@Composable
internal fun TrackCard(
    track: TrackUiModel,
//    showTrackArtists: Boolean = false,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
    onWorkClick: (WorkMusicBrainzModel) -> Unit = {},
    // no onTrackClick needed since Tracks exists in the context of a Release
) {

    ClickableListItem(
        onClick = { onRecordingClick(track.recordingId, track.title) },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            val (startSection, mainSection, endSection) = createRefs()

            Text(
                text = track.number,
                style = TextStyles.getCardBodyTextStyle(),
                modifier = Modifier.constrainAs(startSection) {
                    width = Dimension.wrapContent
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(mainSection.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                }
            )

            Column(
                modifier = Modifier.constrainAs(mainSection) {
                    width = Dimension.fillToConstraints
                    start.linkTo(startSection.end)
                    top.linkTo(parent.top)
                    end.linkTo(endSection.start)
                    bottom.linkTo(parent.bottom)
                }
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

            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodyTextStyle(),
                modifier = Modifier
                    .constrainAs(endSection) {
                        width = Dimension.wrapContent
                        start.linkTo(mainSection.end, margin = 4.dp)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        // TODO: more content in new screen that expands, but maybe not cover the entire screen
    }
}

@ExcludeFromJacocoGeneratedReport
internal class TrackCardPreviewParameterProvider : PreviewParameterProvider<TrackUiModel> {
    override val values = sequenceOf(
        TrackUiModel(
            id = "1",
            title = "Track title",
            position = 1,
            number = "A",
            mediumId = 1L,
            recordingId = "r1"
        ),
        TrackUiModel(
            id = "2",
            title = "Track title that is long and wraps",
            position = 1,
            number = "123",
            length = 25300000,
            mediumId = 2L,
            recordingId = "r2"
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(TrackCardPreviewParameterProvider::class) track: TrackUiModel
) {
    PreviewTheme {
        Surface {
            TrackCard(
                track = track,
//                showTrackArtists = true
            )
        }
    }
}
