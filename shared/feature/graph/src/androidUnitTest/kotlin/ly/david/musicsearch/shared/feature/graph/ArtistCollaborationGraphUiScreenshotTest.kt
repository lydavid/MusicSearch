package ly.david.musicsearch.shared.feature.graph

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistCollaborationGraphUiScreenshotTest : ScreenshotTest() {
    @Test
    fun default() {
        snapshot {
            PreviewArtistCollaborationGraphUi()
        }
    }
}
