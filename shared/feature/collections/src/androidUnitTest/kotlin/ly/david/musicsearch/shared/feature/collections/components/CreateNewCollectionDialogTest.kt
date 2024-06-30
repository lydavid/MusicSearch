package ly.david.musicsearch.shared.feature.collections.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CreateNewCollectionDialogTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewCreateNewCollectionDialogContent()
        }
    }
}
