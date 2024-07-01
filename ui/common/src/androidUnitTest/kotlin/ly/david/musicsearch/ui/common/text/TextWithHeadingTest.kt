package ly.david.musicsearch.ui.common.text

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class TextWithHeadingTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewTextWithHeading()
        }
    }
}
