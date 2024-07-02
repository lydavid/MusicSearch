package ly.david.musicsearch.shared.feature.history

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class HistorySortBottomSheetTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewHistorySortBottomSheetContent()
        }
    }
}
