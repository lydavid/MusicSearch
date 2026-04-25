package ly.david.musicsearch.ui.common.button

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CheckboxWithTextTest : ScreenshotTest() {

    @Test
    fun off() {
        snapshot {
            PreviewCheckboxWithTextOff()
        }
    }

    @Test
    fun on() {
        snapshot {
            PreviewCheckboxWithTextOn()
        }
    }
}
