package ly.david.musicsearch.ui.common.place

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class PlaceListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewPlaceListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewPlaceListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewPlaceListItemVisited()
        }
    }
}
