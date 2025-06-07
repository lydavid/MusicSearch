package ly.david.musicsearch.ui.common.paging

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class FooterLoadingIndicatorTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewFooterLoadingIndicator()
        }
    }
}
