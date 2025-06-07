package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewProfileCardLoggedOut() {
    PreviewTheme {
        Surface {
            ProfileCard(
                username = "bob",
                showLogin = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewProfileCardLoggedIn() {
    PreviewTheme {
        Surface {
            ProfileCard(
                username = "bob",
                showLogin = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewProfileCardLoggedInWaitingForUsername() {
    PreviewTheme {
        Surface {
            ProfileCard(
                username = "",
                showLogin = false,
            )
        }
    }
}
