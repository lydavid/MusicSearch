package ly.david.musicsearch.android.feature.spotify.history

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.spotify.history.PreviewSearchSpotifyBottomSheetContent
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SearchSpotifyBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewSearchSpotifyBottomSheetContent()
        }
    }
}
