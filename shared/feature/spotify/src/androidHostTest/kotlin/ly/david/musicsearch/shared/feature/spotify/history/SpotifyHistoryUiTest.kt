package ly.david.musicsearch.shared.feature.spotify.history

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SpotifyHistoryUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewNowPlayingHistoryUi()
        }
    }
}
