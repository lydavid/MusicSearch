package ly.david.musicsearch.ui.common.dialog

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class MultipleChoiceDialogTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewMultipleChoiceDialog()
        }
    }
}
