package ly.david.musicsearch.shared.feature.nowplaying

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class NowPlayingHistoryCardTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewNowPlayingHistoryCard()
        }
    }
}
