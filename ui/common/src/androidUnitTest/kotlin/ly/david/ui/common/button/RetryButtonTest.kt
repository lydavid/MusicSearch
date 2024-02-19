package ly.david.ui.common.button

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RetryButtonTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            RetryButton()
        }
    }
}
