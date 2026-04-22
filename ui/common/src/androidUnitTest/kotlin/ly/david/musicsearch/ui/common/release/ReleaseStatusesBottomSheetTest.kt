package ly.david.musicsearch.ui.common.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ReleaseStatusesBottomSheetTest : ScreenshotTest() {

    @Test
    fun all() {
        snapshot {
            PreviewReleaseStatusesBottomSheetContentAll()
        }
    }

    @Test
    fun withOneDeselected() {
        snapshot {
            PreviewReleaseStatusesBottomSheetContentWithOneDeselected()
        }
    }

    @Test
    fun withOneSelected() {
        snapshot {
            PreviewReleaseStatusesBottomSheetContentWithOneSelected()
        }
    }
}
