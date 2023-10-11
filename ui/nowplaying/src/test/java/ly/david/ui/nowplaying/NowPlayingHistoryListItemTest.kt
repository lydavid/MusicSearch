package ly.david.ui.nowplaying

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class NowPlayingHistoryListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewNowPlayingCard()
        }
    }
}
