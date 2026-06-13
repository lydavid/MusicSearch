package ly.david.musicsearch.shared.feature.stats

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class StatsUiTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewStatsUi()
        }
    }
}
