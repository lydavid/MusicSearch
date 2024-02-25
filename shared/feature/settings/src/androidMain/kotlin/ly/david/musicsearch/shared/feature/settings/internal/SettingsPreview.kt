package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewSettingsScreen() {
    PreviewTheme {
        Surface {
            Settings(
                versionName = "1.0.0",
                versionCode = 1,
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewSettingsScreenNotificationListenerEnable() {
    PreviewTheme {
        Surface {
            Settings(
                isNotificationListenerEnabled = true,
                versionName = "1.0.0",
                versionCode = 1,
            )
        }
    }
}
