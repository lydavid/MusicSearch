package ly.david.mbjc.ui.release.tracks

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.TrackListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ThreeSectionListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.recording.RecordingListItem
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

/**
 * Also see [RecordingListItem].
 */
@Composable
internal fun TrackListItem(
    track: TrackListItemModel,
//    showTrackArtists: Boolean = false,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {
    ThreeSectionListItem(
        onClick = { onRecordingClick(track.recordingId, track.title) },
        startContent = {
            Text(
                text = track.number,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        startMainPadding = 16.dp,
        mainContent = {
            Text(
                text = track.title,
                style = TextStyles.getCardTitleTextStyle(),
            )
        },
        endMainPadding = 4.dp,
        endContent = {
            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodyTextStyle()
            )
        }
    )
}

@ExcludeFromJacocoGeneratedReport
internal class TrackCardPreviewParameterProvider : PreviewParameterProvider<TrackListItemModel> {
    override val values = sequenceOf(
        TrackListItemModel(
            id = "1",
            title = "Track title",
            position = 1,
            number = "A",
            mediumId = 1L,
            recordingId = "r1"
        ),
        TrackListItemModel(
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
    @PreviewParameter(TrackCardPreviewParameterProvider::class) track: TrackListItemModel
) {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = track,
//                showTrackArtists = true
            )
        }
    }
}
