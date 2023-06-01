package ly.david.ui.common.dialog

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SimpleAlertDialogTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun long() {
        snapshot {
            SimpleAlertDialog(
                title = "Title text that is long enough to wrap",
                confirmText = "OK"
            ) {}
        }
    }
}
