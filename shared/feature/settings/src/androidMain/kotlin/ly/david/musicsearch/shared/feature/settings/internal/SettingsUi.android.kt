package ly.david.musicsearch.shared.feature.settings.internal

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.app.NotificationManagerCompat
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
internal actual fun SettingsUi(
    state: SettingsUiState,
    modifier: Modifier,
) {
    val context = LocalContext.current

    SettingsUi(
        state = state,
        showAndroidSettings = true,
        modifier = modifier,
        isNotificationListenerEnabled = context.isNotificationListenerEnabled(),
        onGoToNotificationListenerSettings = {
            context.startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        },
    )
}

private fun Context.isNotificationListenerEnabled(): Boolean {
    return NotificationManagerCompat.getEnabledListenerPackages(this).any { it == this.packageName }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreen() {
    PreviewTheme {
        Surface {
            SettingsUi(
                versionName = "1.0.0",
                versionCode = 1,
                databaseVersion = "1",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenNotificationListenerEnable() {
    PreviewTheme {
        Surface {
            SettingsUi(
                isNotificationListenerEnabled = true,
                versionName = "1.0.0",
                versionCode = 1,
                databaseVersion = "1",
            )
        }
    }
}
