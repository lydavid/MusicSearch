package ly.david.musicsearch.shared.feature.search

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SearchUiScreenshotTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun searchResults() {
        snapshot {
            PreviewSearchUiSearchResults()
        }
    }

    @Test
    fun release() {
        snapshot {
            PreviewSearchUiSearchHistory()
        }
    }
}
