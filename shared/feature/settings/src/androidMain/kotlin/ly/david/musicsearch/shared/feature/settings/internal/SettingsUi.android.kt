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
import ly.david.musicsearch.shared.domain.listen.ListenBrainzLoginState
import ly.david.musicsearch.ui.common.preview.PreviewTheme

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

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenAndroid() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                ),
                showAndroidSettings = true,
                isNotificationListenerEnabled = false,
                versionName = "1.2.3",
                versionCode = 123,
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
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                ),
                showAndroidSettings = true,
                isNotificationListenerEnabled = true,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenAndroidWithCrashReporting() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                    showCrashReporterSettings = true,
                ),
                showAndroidSettings = true,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenNonAndroid() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                ),
                showAndroidSettings = false,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenLoggedIn() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                    musicBrainzUsername = "david",
                    musicBrainzAccessToken = "token",
                    listenBrainzUsername = "david-lb",
                    listenBrainzUserToken = "token-lb",
                    listenBrainzLoginState = ListenBrainzLoginState.LoggedIn,
                ),
                showAndroidSettings = false,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenLoggedOut() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                    musicBrainzUsername = "david",
                    musicBrainzAccessToken = "token",
                    listenBrainzLoginState = ListenBrainzLoginState.LoggedOut,
                ),
                showAndroidSettings = false,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenInvalidToken() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                    musicBrainzUsername = "david",
                    musicBrainzAccessToken = "token",
                    listenBrainzLoginState = ListenBrainzLoginState.InvalidToken,
                ),
                showAndroidSettings = false,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingsScreenOtherError() {
    PreviewTheme {
        Surface {
            SettingsUi(
                state = SettingsUiState(
                    appDatabaseVersion = "1",
                    musicBrainzUsername = "david",
                    musicBrainzAccessToken = "token",
                    listenBrainzLoginState = ListenBrainzLoginState.OtherError("error"),
                ),
                showAndroidSettings = false,
                versionName = "1.2.3",
                versionCode = 123,
            )
        }
    }
}
// endregion
