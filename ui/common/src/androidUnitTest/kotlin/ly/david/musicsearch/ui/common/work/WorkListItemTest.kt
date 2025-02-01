package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class WorkListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewWorkListItemModel()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewWorkListItemModelAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewWorkListItemModelVisited()
        }
    }
}
