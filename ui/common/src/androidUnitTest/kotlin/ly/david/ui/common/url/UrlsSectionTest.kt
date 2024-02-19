package ly.david.ui.common.url

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class UrlsSectionTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            ly.david.ui.common.url.PreviewUrlsSection()
        }
    }
}
