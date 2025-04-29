package ly.david.musicsearch.ui.common.listitem

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LastUpdatedFooterItemTest : ScreenshotTest() {

    @Test
    fun lastUpdatedFooterItem() {
        snapshot {
            PreviewLastUpdatedFooterItem()
        }
    }

    @Test
    fun lastUpdatedText() {
        snapshot {
            PreviewLastUpdatedText()
        }
    }
}
