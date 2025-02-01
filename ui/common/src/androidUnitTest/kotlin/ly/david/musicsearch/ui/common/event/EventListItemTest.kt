package ly.david.musicsearch.ui.common.event

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class EventListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewEventListItem()
        }
    }

    @Test
    fun long() {
        snapshot {
            PreviewEventListItemLong()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewEventListItemVisited()
        }
    }
}
