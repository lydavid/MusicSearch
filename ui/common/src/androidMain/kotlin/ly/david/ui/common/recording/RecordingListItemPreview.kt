package ly.david.ui.common.recording

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

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
