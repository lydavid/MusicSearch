package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewFullScreenErrorWithActionableButtonRetry() {
    PreviewTheme {
        Surface {
            FullScreenErrorWithActionableButton()
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewFullScreenErrorWithActionableButtonLogin() {
    PreviewTheme {
        Surface {
            FullScreenErrorWithActionableButton(
                handledException = HandledException(
                    userMessage = "You need to login",
                    errorResolution = ErrorResolution.Login,
                )
            )
        }
    }
}
