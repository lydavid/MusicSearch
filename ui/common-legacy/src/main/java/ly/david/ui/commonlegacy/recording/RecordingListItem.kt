package ly.david.ui.commonlegacy.recording

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
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toDisplayTime
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.common.release.TrackListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

/**
 * Also see [TrackListItem].
 */
@Composable
fun RecordingListItem(
    recording: RecordingListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: RecordingListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = recording.name,
                style = TextStyles.getCardBodyTextStyle(),
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
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                recording.firstReleaseDate.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                Text(
                    text = recording.length.toDisplayTime(),
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        },
    )
}

// region Previews
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
            formattedArtistCredits = "Some artist feat. Other artist",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(RecordingPreviewParameterProvider::class) recording: RecordingListItemModel,
) {
    PreviewTheme {
        Surface {
            RecordingListItem(recording = recording)
        }
    }
}
// endregion
