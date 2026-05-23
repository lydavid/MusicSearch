package ly.david.musicsearch.shared.feature.nowplaying

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class NowPlayingHistoryUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewNowPlayingHistoryUi()
        }
    }
}
