package ly.david.musicsearch.ui.common.text

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TextWithHeadingTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewTextWithHeading()
        }
    }
}
