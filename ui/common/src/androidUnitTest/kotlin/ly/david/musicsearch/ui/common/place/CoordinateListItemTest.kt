package ly.david.musicsearch.ui.common.place

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CoordinateListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewCoordinateListItem()
        }
    }
}
