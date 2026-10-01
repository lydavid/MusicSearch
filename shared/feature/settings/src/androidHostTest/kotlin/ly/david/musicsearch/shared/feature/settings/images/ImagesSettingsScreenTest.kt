package ly.david.musicsearch.shared.feature.settings.images

import ly.david.musicsearch.shared.feature.settings.internal.images.PreviewImagesSettingsUi
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ImagesSettingsScreenTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewImagesSettingsUi()
        }
    }
}
