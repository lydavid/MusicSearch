package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoverArtsUiTest : ScreenshotTest() {

    @Test
    fun one() {
        snapshot {
            PreviewCoverArtsUiOne()
        }
    }

    @Test
    fun multiple() {
        snapshot {
            PreviewCoverArtsUiMultiple()
        }
    }
}
