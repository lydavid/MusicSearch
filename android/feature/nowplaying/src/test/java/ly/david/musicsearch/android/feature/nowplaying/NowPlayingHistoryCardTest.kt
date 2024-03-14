package ly.david.musicsearch.android.feature.nowplaying

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.android.feature.nowplaying.internal.PreviewNowPlayingHistoryCard
import ly.david.ui.test.screenshot.ScreenshotTest
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
