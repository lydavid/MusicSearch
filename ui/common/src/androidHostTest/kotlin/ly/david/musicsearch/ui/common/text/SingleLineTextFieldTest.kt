package ly.david.musicsearch.ui.common.text

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SingleLineTextFieldTest : ScreenshotTest() {

    @Test
    fun empty() {
        snapshot {
            PreviewSingleLineTextFieldEmpty()
        }
    }

    @Test
    fun withText() {
        snapshot {
            PreviewSingleLineTextFieldWithText()
        }
    }
}
