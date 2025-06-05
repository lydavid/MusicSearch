package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewRecordingDetailsUi() {
    PreviewTheme {
        Surface {
            RecordingDetailsTabUi(
                recording = RecordingDetailsModel(
                    id = "132a508b-624a-4f1d-b61f-f6616121bab5",
                    name = "プライド革命",
                    length = 235000,
                    isrcs = listOf(
                        "JPX401500068",
                    ),
                ),
            )
        }
    }
}
