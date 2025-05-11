package ly.david.musicsearch.ui.common.url

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class UrlsSectionTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewUrlsSection()
        }
    }
}
