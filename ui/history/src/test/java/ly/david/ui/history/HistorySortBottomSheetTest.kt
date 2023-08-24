package ly.david.ui.history

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class HistorySortBottomSheetTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewHistorySortBottomSheetContent()
        }
    }
}
