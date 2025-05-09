package ly.david.musicsearch.shared.feature.settings

import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreen
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenNotificationListenerEnable
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SettingsScreenTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewSettingsScreen()
        }
    }

    @Test
    fun notificationListenerEnable() {
        snapshot {
            PreviewSettingsScreenNotificationListenerEnable()
        }
    }
}
