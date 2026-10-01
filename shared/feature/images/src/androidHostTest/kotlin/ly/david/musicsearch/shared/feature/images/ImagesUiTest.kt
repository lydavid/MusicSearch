package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ImagesUiTest : ScreenshotTest() {

    @Test
    fun imagesGrid() {
        snapshot {
            PreviewImagesGridUi()
        }
    }

    @Test
    fun imagesPagerCompact() {
        snapshot {
            PreviewImagesPagerUiCompact()
        }
    }

    @Test
    fun imagesPagerNonCompact() {
        snapshot {
            PreviewImagesPagerUiNonCompact()
        }
    }
}
