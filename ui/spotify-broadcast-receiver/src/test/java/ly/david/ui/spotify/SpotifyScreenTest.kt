package ly.david.ui.spotify

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SpotifyScreenTest : ScreenshotTest() {

    @Test
    fun empty() {
        snapshot {
            PreviewSpotifyScreenEmpty()
        }
    }

    @Test
    fun withData() {
        snapshot {
            PreviewSpotifyScreenWithData()
        }
    }
}
