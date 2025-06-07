package ly.david.musicsearch.ui.common.listitem

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class DisambiguationTextTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewDisambiguationText()
        }
    }
}
