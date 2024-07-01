package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.settings.internal.components.ProfileCard
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
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

@DefaultPreviews
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

@DefaultPreviews
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
