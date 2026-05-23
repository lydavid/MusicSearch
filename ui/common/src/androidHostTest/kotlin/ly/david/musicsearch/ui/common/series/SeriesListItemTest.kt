package ly.david.musicsearch.ui.common.series

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SeriesListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewSeriesListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewSeriesListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewSeriesListItemVisited()
        }
    }
}
