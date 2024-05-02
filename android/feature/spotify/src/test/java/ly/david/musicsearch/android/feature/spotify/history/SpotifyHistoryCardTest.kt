package ly.david.musicsearch.android.feature.spotify.history

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SpotifyHistoryCardTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewSpotifyHistoryCard()
        }
    }
}
