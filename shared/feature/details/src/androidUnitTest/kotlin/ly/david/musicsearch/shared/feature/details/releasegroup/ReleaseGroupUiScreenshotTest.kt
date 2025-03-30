package ly.david.musicsearch.shared.feature.details.releasegroup

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ReleaseGroupUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewReleaseGroupDetailsUi()
        }
    }
}
