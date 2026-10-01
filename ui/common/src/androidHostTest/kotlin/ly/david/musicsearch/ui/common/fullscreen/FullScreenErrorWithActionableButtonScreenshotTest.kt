package ly.david.musicsearch.ui.common.fullscreen

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class FullScreenErrorWithActionableButtonScreenshotTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun retry() {
        snapshot {
            PreviewFullScreenErrorWithActionableButtonRetry()
        }
    }

    @Test
    fun login() {
        snapshot {
            PreviewFullScreenErrorWithActionableButtonLogin()
        }
    }
}
