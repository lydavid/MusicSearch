package ly.david.musicsearch.shared.feature.details.artist

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewArtistDetailsUi()
        }
    }

    @Test
    fun collapsed() {
        snapshot {
            PreviewArtistDetailsUiCollapsed()
        }
    }

    @Test
    fun withWikipediaUrlButNoExtract() {
        snapshot {
            PreviewArtistDetailsUiWithWikipediaUrlButNoExtract()
        }
    }
}
