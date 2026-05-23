package ly.david.musicsearch.shared.feature.listens

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ListenListItemScreenshotTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewListenListItem()
        }
    }
}
