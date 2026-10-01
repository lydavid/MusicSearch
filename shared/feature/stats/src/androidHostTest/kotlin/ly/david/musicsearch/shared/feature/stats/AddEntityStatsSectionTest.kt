package ly.david.musicsearch.shared.feature.stats

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AddEntityStatsSectionTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewAddEntityStatsSection()
        }
    }

    @Test
    fun releaseGroup() {
        snapshot {
            PreviewAddEntityStatsSectionReleaseGroup()
        }
    }
}
