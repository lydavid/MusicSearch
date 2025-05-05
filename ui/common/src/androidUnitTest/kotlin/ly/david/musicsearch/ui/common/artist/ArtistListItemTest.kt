package ly.david.musicsearch.ui.common.artist

import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
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
            ArtistListItem(
                artist = ArtistListItemModel(
                    id = "1",
                    name = "Artist name",
                    sortName = "",
                    imageUrl = "https://www.example.com/image.jpg",
                ),
            )
        }
    }

    @Test
    fun selected() {
        snapshot {
            PreviewArtistListItemSelected()
        }
    }
}
