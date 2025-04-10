package ly.david.musicsearch.shared.feature.settings.internal

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.app.NotificationManagerCompat
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
internal actual fun Settings(
    state: SettingsUiState,
    modifier: Modifier,
) {
    val context = LocalContext.current

    Settings(
        state = state,
        showAndroidSettings = true,
        modifier = modifier,
        isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
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
            Settings(
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
            Settings(
                isNotificationListenerEnabled = true,
                versionName = "1.0.0",
                versionCode = 1,
                databaseVersion = "1",
            )
        }
    }
}
