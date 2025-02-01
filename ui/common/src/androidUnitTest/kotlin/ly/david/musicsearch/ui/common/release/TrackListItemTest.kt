package ly.david.musicsearch.ui.common.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TrackListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewTrackListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewTrackListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewTrackListItemVisited()
        }
    }
}
