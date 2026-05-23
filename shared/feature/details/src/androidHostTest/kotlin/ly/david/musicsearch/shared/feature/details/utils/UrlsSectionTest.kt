package ly.david.musicsearch.shared.feature.details.utils

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
