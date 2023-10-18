package ly.david.musicsearch.android.feature.spotify

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.android.feature.spotify.internal.PreviewSpotifyScreenEmpty
import ly.david.musicsearch.android.feature.spotify.internal.PreviewSpotifyScreenWithData
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
