package ly.david.ui.common.topappbar

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class DotsFlashingTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            DotsFlashing()
        }
    }
}
