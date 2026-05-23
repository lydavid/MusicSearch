package ly.david.musicsearch.shared.feature.details.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TracksByReleaseUiTest : ScreenshotTest() {
    @Test
    fun mix() {
        snapshot {
            PreviewTracksByReleaseUi()
        }
    }
}
