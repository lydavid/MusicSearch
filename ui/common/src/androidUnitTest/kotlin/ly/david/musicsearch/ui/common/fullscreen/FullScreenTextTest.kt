package ly.david.musicsearch.ui.common.fullscreen

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class FullScreenTextTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewFullScreenText()
        }
    }
}
