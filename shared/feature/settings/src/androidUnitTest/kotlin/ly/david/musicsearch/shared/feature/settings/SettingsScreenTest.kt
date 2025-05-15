package ly.david.musicsearch.shared.feature.settings

import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenAndroid
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenAndroidWithCrashReporting
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenLoggedIn
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenNonAndroid
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenNotificationListenerEnable
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SettingsScreenTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun android() {
        snapshot {
            PreviewSettingsScreenAndroid()
        }
    }

    @Test
    fun notificationListenerEnable() {
        snapshot {
            PreviewSettingsScreenNotificationListenerEnable()
        }
    }

    @Test
    fun androidWithCrashReporting() {
        snapshot {
            PreviewSettingsScreenAndroidWithCrashReporting()
        }
    }

    @Test
    fun nonAndroid() {
        snapshot {
            PreviewSettingsScreenNonAndroid()
        }
    }

    @Test
    fun loggedIn() {
        snapshot {
            PreviewSettingsScreenLoggedIn()
        }
    }
}
