package ly.david.musicsearch.shared.feature.details.area

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AreaUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewAreaDetails()
        }
    }

    @Test
    fun detailsCollapsed() {
        snapshot {
            PreviewAreaDetailsCollapsed()
        }
    }

    @Test
    fun detailsError() {
        snapshot {
            PreviewAreaDetailsError()
        }
    }

    @Test
    fun relationships() {
        snapshot {
            PreviewAreaRelationships()
        }
    }

    @Test
    fun artists() {
        snapshot {
            PreviewAreaArtists()
        }
    }

    @Test
    fun events() {
        snapshot {
            PreviewAreaEvents()
        }
    }

    @Test
    fun labels() {
        snapshot {
            PreviewAreaLabels()
        }
    }

    @Test
    fun releases() {
        snapshot {
            PreviewAreaReleases()
        }
    }

    @Test
    fun places() {
        snapshot {
            PreviewAreaPlaces()
        }
    }
}
