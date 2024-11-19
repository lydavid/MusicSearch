package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoverArtsPagerUiTest : ScreenshotTest() {

    @Test
    fun coverArtsPagerCompact() {
        snapshot {
            PreviewCoverArtsPagerUiCompact()
        }
    }

    @Test
    fun coverArtsPagerNonCompact() {
        snapshot {
            PreviewCoverArtsPagerUiNonCompact()
        }
    }
}
