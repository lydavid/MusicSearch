package ly.david.musicsearch.ui.common.artist

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewArtistListItemSimple()
        }
    }

    @Test
    fun countryCode() {
        snapshot {
            PreviewArtistListItemCountryCode()
        }
    }

    @Test
    fun allInfoUnvisited() {
        snapshot {
            PreviewArtistListItemAllInfoUnvisited()
        }
    }

    @Test
    fun allInfoVisited() {
        snapshot {
            PreviewArtistListItemAllInfoVisited()
        }
    }

    @Test
    fun withCoverArt() {
        snapshot {
            PreviewArtistListItemWithCoverArt()
        }
    }

    @Test
    fun selected() {
        snapshot {
            PreviewArtistListItemSelected()
        }
    }
}
