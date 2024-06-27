package ly.david.musicsearch.shared.feature.details.area

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class AreaDetailsUiTest : ScreenshotTest() {

    @Test
    fun isRemote() {
        snapshot {
            PreviewAreaDetailsUi()
        }
    }
}
