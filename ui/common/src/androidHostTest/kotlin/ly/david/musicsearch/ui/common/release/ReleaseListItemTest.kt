package ly.david.musicsearch.ui.common.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ReleaseListItemTest : ScreenshotTest() {

    @Test
    fun releaseListItem() {
        snapshot {
            PreviewReleaseListItem()
        }
    }

    @Test
    fun releaseListItemDisambiguation() {
        snapshot {
            PreviewReleaseListItemDisambiguation()
        }
    }

    @Test
    fun releaseListItemDisambiguationCountry() {
        snapshot {
            PreviewReleaseListItemDisambiguationCountry()
        }
    }

    @Test
    fun releaseListItemCountry() {
        snapshot {
            PreviewReleaseListItemCountry()
        }
    }

    @Test
    fun releaseListItemDateCountryFormatsTracks() {
        snapshot {
            PreviewReleaseListItemDateCountryFormatsTracks()
        }
    }

    @Test
    fun releaseListItemCountryDate() {
        snapshot {
            PreviewReleaseListItemCountryDate()
        }
    }

    @Test
    fun releaseListItemDateArtistCredits() {
        snapshot {
            PreviewReleaseListItemDateArtistCredits()
        }
    }

    @Test
    fun releaseListItemMultipleCountries() {
        snapshot {
            PreviewReleaseListItemMultipleCountries()
        }
    }

    @Test
    fun releaseListItemShowLessInfo() {
        snapshot {
            PreviewReleaseListItemShowLessInfo()
        }
    }

    @Test
    fun releaseListItemCatalog() {
        snapshot {
            PreviewReleaseListItemCatalog()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewReleaseListItemVisited()
        }
    }

    @Test
    fun withListens() {
        snapshot {
            PreviewReleaseListItemWithListens()
        }
    }

    @Test
    fun withUnknownNumberOfListens() {
        snapshot {
            PreviewReleaseListItemWithUnknownNumberOfListens()
        }
    }

    @Test
    fun withCoverArt() {
        snapshot {
            PreviewReleaseListItemWithCoverArt()
        }
    }
}
