package ly.david.ui.stats

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class StatsScreenTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewStatsScreen()
        }
    }
}
