package ly.david.musicsearch.ui.common.wikimedia

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class WikipediaSectionTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewWikipediaSection()
        }
    }
}
