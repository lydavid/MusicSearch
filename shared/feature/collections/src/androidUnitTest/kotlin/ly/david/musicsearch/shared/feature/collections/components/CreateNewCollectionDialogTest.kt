package ly.david.musicsearch.shared.feature.collections.components

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CreateNewCollectionDialogTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewCreateNewCollectionDialogContent()
        }
    }

    @Test
    fun withDefaultEntity() {
        snapshot {
            PreviewCreateNewCollectionDialogContentDefaultEntity()
        }
    }
}
