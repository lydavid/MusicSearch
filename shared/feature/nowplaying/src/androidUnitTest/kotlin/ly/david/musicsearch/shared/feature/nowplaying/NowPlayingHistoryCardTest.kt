package ly.david.musicsearch.shared.feature.nowplaying

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.nowplaying.PreviewNowPlayingHistoryCard
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class NowPlayingHistoryCardTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewNowPlayingHistoryCard()
        }
    }
}
