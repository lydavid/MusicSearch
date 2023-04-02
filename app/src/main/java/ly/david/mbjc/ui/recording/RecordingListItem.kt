package ly.david.mbjc.ui.recording

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.RecordingListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.release.tracks.TrackListItem
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

/**
 * Also see [TrackListItem].
 */
@Composable
internal fun RecordingListItem(
    recording: RecordingListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: RecordingListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineContent = {
            Text(
                text = recording.name,
                style = TextStyles.getCardTitleTextStyle(),
            )
        },
        modifier = modifier.clickable {
            onRecordingClick(recording)
        },
        supportingContent = {
            Column {
                DisambiguationText(disambiguation = recording.disambiguation)

                recording.formattedArtistCredits.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        style = TextStyles.getCardBodyTextStyle()
                    )
                }
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                recording.firstReleaseDate.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                Text(
                    text = recording.length.toDisplayTime(),
                    style = TextStyles.getCardBodyTextStyle()
                )
            }
        }
    )
}

// region Previews
@ExcludeFromJacocoGeneratedReport
internal class RecordingPreviewParameterProvider : PreviewParameterProvider<RecordingListItemModel> {
    override val values = sequenceOf(
        RecordingListItemModel(
            id = "1",
            name = "Recording name",
        ),
        RecordingListItemModel(
            id = "2",
            name = "Recording name",
            firstReleaseDate = "2022-05-23",
            disambiguation = "that one",
            length = 25300000,
            video = false,
            formattedArtistCredits = "Some artist feat. Other artist"
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(RecordingPreviewParameterProvider::class) recording: RecordingListItemModel
) {
    PreviewTheme {
        Surface {
            RecordingListItem(recording = recording)
        }
    }
}
// endregion
