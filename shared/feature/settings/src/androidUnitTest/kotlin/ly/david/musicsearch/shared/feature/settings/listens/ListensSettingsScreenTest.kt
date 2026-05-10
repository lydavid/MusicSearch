package ly.david.musicsearch.shared.feature.settings.listens

import ly.david.musicsearch.shared.feature.settings.internal.listens.PreviewListensSettingsUi
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ListensSettingsScreenTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewListensSettingsUi()
        }
    }
}
