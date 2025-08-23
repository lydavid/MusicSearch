package ly.david.musicsearch.shared.feature.listens

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ListensUiScreenshotTest : ScreenshotTest() {

    @Test
    fun list() {
        snapshot {
            PreviewListensUi()
        }
    }

    @Test
    fun noUsername() {
        snapshot {
            PreviewListensUiNoUsername()
        }
    }

    @Test
    fun bottomSheetContent() {
        snapshot {
            PreviewListensUiBottomSheetContent()
        }
    }
}
