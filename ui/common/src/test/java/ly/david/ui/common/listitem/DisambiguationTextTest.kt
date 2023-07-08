package ly.david.ui.common.listitem

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class DisambiguationTextTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            DisambiguationText(disambiguation = "Disambiguation text")
        }
    }
}
