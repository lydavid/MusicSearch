package ly.david.musicsearch.shared.feature.settings.internal.behaviour

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewBehaviourSettingsUi() {
    PreviewTheme {
        Surface {
            BehaviourSettingsUi(
                state = BehaviourSettingsUiState(),
            )
        }
    }
}
