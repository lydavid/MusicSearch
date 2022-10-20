package ly.david.mbjc.ui.recording

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.RecordingUiModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Compare with [ly.david.mbjc.ui.release.tracks.TrackCard].
 */
@Composable
internal fun RecordingCard(
    recordingUiModel: RecordingUiModel,
    onRecordingClick: RecordingUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onRecordingClick(recordingUiModel) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            // TODO: make it look better
            Text(text = recordingUiModel.name)
            Text(text = recordingUiModel.disambiguation)
            Text(text = recordingUiModel.date.orEmpty())
            Text(text = recordingUiModel.length.toDisplayTime())
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun RecordingCardPreview() {
    PreviewTheme {
        Surface {
            RecordingCard(recordingUiModel = RecordingUiModel(
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
