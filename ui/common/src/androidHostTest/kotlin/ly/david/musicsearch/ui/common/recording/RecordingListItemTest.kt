package ly.david.musicsearch.ui.common.recording

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class RecordingListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewRecordingListItem()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewRecordingListItemAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewRecordingListItemVisited()
        }
    }
}
