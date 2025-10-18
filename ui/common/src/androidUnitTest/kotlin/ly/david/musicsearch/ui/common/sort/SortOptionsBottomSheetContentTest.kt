package ly.david.musicsearch.ui.common.sort

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SortOptionsBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun none() {
        snapshot {
            PreviewSortOptionsBottomSheetContentNone()
        }
    }

    @Test
    fun dateAscending() {
        snapshot {
            PreviewSortOptionsBottomSheetContentByDateAscending()
        }
    }

    @Test
    fun dateDescending() {
        snapshot {
            PreviewSortOptionsBottomSheetContentByDateDescending()
        }
    }

    @Test
    fun completeListensAscending() {
        snapshot {
            PreviewSortOptionsBottomSheetContentByCompleteListensAscending()
        }
    }

    @Test
    fun completeListensDescending() {
        snapshot {
            PreviewSortOptionsBottomSheetContentByCompleteListensDescending()
        }
    }
}
