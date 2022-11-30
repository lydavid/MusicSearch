package ly.david.mbjc.ui.recording

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.RecordingUiModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.release.tracks.TrackCard
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Also see [TrackCard].
 */
@Composable
internal fun RecordingCard(
    recording: RecordingUiModel,
    onRecordingClick: RecordingUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onRecordingClick(recording) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            // TODO: make it look better

            Text(text = recording.name)
            Text(text = recording.disambiguation)
            Text(text = recording.date.orEmpty())
            Text(text = recording.length.toDisplayTime())
        }
    }
}



@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            RecordingCard(
                recording = RecordingUiModel(
                    id = "1",
                    name = "Recording name",
                    date = "2022-05-23",
                    disambiguation = "that one",
                    length = 25300000,
                    video = false
                )
            )
        }
    }
}
