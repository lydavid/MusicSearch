package ly.david.musicsearch.ui.common.button

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TriStateCheckboxWithTextTest : ScreenshotTest() {

    @Test
    fun off() {
        snapshot {
            PreviewTriStateCheckboxWithTextOff()
        }
    }

    @Test
    fun indeterminate() {
        snapshot {
            PreviewTriStateCheckboxWithTextIndeterminate()
        }
    }

    @Test
    fun on() {
        snapshot {
            PreviewTriStateCheckboxWithTextOn()
        }
    }
}
