package ly.david.ui.common.fullscreen

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class FullScreenLoadingIndicatorTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            FullScreenLoadingIndicator()
        }
    }
}
