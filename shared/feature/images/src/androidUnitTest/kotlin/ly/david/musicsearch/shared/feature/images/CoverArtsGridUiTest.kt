package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoverArtsGridUiTest : ScreenshotTest() {

    @Test
    fun coverArtsGrid() {
        snapshot {
            PreviewCoverArtsGridUi()
        }
    }
}
