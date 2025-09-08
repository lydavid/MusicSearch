package ly.david.musicsearch.shared.feature.details.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ReleaseUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewReleaseDetailsUi()
        }
    }

    @Test
    fun collapsed() {
        snapshot {
            PreviewReleaseDetailsUiCollapsed()
        }
    }

    @Test
    fun withListens() {
        snapshot {
            PreviewReleaseDetailsUiWithListens()
        }
    }
}
