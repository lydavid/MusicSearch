package ly.david.ui.common.fullscreen

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class FullScreenLoadingIndicatorTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            FullScreenLoadingIndicator()
        }
    }
}
