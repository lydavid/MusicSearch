package ly.david.musicsearch.android.feature.spotify

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.android.feature.spotify.internal.PreviewSpotifyUi
import ly.david.musicsearch.android.feature.spotify.internal.PreviewSpotifyUiEmpty
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SpotifyUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun empty() {
        snapshot {
            PreviewSpotifyUi()
        }
    }

    @Test
    fun withData() {
        snapshot {
            PreviewSpotifyUiEmpty()
        }
    }
}
