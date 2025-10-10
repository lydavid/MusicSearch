package ly.david.musicsearch.ui.common.recording

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class RecordingSortOptionBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun none() {
        snapshot {
            PreviewRecordingSortBottomSheetContentNone()
        }
    }

    @Test
    fun dateAscending() {
        snapshot {
            PreviewRecordingSortBottomSheetContentByDateAscending()
        }
    }

    @Test
    fun dateDescending() {
        snapshot {
            PreviewRecordingSortBottomSheetContentByDateDescending()
        }
    }
}
