package ly.david.ui.common.button

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RetryButtonTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            RetryButton()
        }
    }
}
