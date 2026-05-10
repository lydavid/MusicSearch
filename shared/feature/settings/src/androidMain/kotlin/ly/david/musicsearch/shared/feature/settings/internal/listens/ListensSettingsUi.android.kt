package ly.david.musicsearch.shared.feature.settings.internal.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_LATEST_LISTENS_TO_SHOW
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewListensSettingsUi() {
    PreviewTheme {
        Surface {
            ListensSettingsUi(
                state = ListensSettingsUiState(
                    numberOfListensInDetails = DEFAULT_NUMBER_OF_LATEST_LISTENS_TO_SHOW,
                    submitClientAndVersionWithListen = false,
                ),
            )
        }
    }
}
