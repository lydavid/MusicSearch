package ly.david.musicsearch.shared.feature.settings.services

import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewServicesSettingsUi
import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewServicesSettingsUiCustom
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ServicesSettingsUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewServicesSettingsUi()
        }
    }

    @Test
    fun custom() {
        snapshot {
            PreviewServicesSettingsUiCustom()
        }
    }
}
