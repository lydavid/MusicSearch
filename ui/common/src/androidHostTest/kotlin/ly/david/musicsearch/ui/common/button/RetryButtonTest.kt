package ly.david.musicsearch.ui.common.button

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class RetryButtonTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewRetryButton()
        }
    }
}
