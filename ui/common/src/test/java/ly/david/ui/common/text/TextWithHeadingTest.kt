package ly.david.ui.common.text

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.R
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class TextWithHeadingTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            TextWithHeadingRes(headingRes = R.string.format, text = "Digital Media")
        }
    }
}
