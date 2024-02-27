package ly.david.musicsearch.shared.feature.settings

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreen
import ly.david.musicsearch.shared.feature.settings.internal.PreviewSettingsScreenNotificationListenerEnable
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
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
