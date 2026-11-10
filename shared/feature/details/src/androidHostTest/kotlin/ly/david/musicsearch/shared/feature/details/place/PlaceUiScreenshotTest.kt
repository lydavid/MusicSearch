package ly.david.musicsearch.shared.feature.details.place

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class PlaceUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewPlaceDetailsUi()
        }
    }

    @Test
    fun withoutCoordinates() {
        snapshot {
            PreviewPlaceDetailsUiWithoutCoordinates()
        }
    }

    @Test
    fun filter() {
        snapshot {
            PreviewPlaceDetailsUiAlternative()
        }
    }
}
