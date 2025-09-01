package ly.david.musicsearch.shared.feature.details.label

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LabelUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewLabelDetailsUi()
        }
    }
}
