package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewRecordingDetailsUi() {
    PreviewWithSharedElementTransition {
        RecordingDetailsTabUi(
            recording = RecordingDetailsModel(
                id = "132a508b-624a-4f1d-b61f-f6616121bab5",
                name = "プライド革命",
                length = 235000,
                isrcs = persistentListOf(
                    "JPX401500068",
                ),
            ),
        )
    }
}
