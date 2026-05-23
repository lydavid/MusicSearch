package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class WorkListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewWorkListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewWorkListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewWorkListItemVisited()
        }
    }

    @Test
    fun unknownListens() {
        snapshot {
            PreviewWorkListItemUnknownListens()
        }
    }
}
