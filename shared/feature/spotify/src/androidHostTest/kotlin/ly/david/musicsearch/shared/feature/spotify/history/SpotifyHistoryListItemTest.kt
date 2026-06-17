package ly.david.musicsearch.shared.feature.spotify.history

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SpotifyHistoryListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewSpotifyHistoryListItem()
        }
    }
}
