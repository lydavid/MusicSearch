package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoverArtsUiTest : ScreenshotTest() {

    @Test
    fun compact() {
        snapshot {
            PreviewCoverArtsUiCompact()
        }
    }

    @Test
    fun nonCompact() {
        snapshot {
            PreviewCoverArtsUiNonCompact()
        }
    }
}
