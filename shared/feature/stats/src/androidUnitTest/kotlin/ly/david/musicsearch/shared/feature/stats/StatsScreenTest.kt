package ly.david.musicsearch.shared.feature.stats

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class StatsScreenTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewStatsScreen()
        }
    }
}
