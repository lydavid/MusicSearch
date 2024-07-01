package ly.david.musicsearch.shared.feature.history

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class HistorySortBottomSheetTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewHistorySortBottomSheetContent()
        }
    }
}
