package ly.david.ui.settings

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SettingsScreenTest : PaparazziScreenshotTest(isFullScreen = true) {

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
