package ly.david.musicsearch.ui.common.dialog

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SimpleAlertDialogTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewSimpleAlertDialog()
        }
    }
}
