package ly.david.ui.common.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.TrackListItemModel
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.recording.RecordingListItem
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

/**
 * Also see [RecordingListItem].
 */
@Composable
fun TrackListItem(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
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
        },
        supportingContent = {
            track.formattedArtistCredits.ifNotNullOrEmpty {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    style = TextStyles.getCardBodySubTextStyle()
                )
            }
        }
    )
}

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
            recordingId = "r2",
            formattedArtistCredits = "Some artist feat. Other artist"
        )
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(TrackCardPreviewParameterProvider::class) track: TrackListItemModel
) {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = track,
            )
        }
    }
}
