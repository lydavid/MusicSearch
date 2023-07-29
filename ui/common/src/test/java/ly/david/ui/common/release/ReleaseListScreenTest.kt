package ly.david.ui.common.release

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ReleaseListScreenTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewReleasesListScreen()
        }
    }
}
