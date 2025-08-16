package ly.david.musicsearch.ui.common.component

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class MultipleChoiceBottomSheetTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewMultipleChoiceBottomSheetContent()
        }
    }
}
