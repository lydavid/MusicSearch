package ly.david.ui.collections

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CreateCollectionDialogTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            CreateCollectionDialog()
        }
    }
}
