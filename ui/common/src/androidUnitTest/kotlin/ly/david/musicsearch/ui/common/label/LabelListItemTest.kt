package ly.david.musicsearch.ui.common.label

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LabelListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewLabelListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewLabelListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewLabelListItemVisited()
        }
    }
}
