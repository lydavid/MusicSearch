package ly.david.ui.spotify

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SpotifyScaffoldTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewSpotifyScaffold()
        }
    }
}
