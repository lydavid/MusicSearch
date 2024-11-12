package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoverArtsUiTest : ScreenshotTest() {

    @Test
    fun coverArtsGrid() {
        snapshot {
            PreviewCoverArtsGrid()
        }
    }

    @Test
    fun coverArtsPagerCompact() {
        snapshot {
            PreviewCoverArtsPagerCompact()
        }
    }

    @Test
    fun coverArtsPagerNonCompact() {
        snapshot {
            PreviewCoverArtsPagerNonCompact()
        }
    }
}
