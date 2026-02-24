package ly.david.musicsearch.shared.feature.details.work

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class WorkUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewWorkDetailsUi()
        }
    }

    @Test
    fun detailsWithListens() {
        snapshot {
            PreviewWorkDetailsUiWithListens()
        }
    }
}
