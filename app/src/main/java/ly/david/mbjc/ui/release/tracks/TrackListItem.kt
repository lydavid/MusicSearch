package ly.david.mbjc.ui.release.tracks

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.TrackListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
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
    modifier: Modifier = Modifier,
//    showTrackArtists: Boolean = false,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {
    ListItem(
        headlineContent = {
            Text(
                text = track.title,
                style = TextStyles.getCardTitleTextStyle(),
            )
        },
        modifier = modifier.clickable {
            onRecordingClick(track.recordingId, track.title)
        },
        leadingContent = {
            Text(
                text = track.number,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        trailingContent = {
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
