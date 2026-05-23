package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class DotsFlashingTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewDotsFlashing()
        }
    }
}
