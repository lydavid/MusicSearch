package ly.david.musicsearch.shared.feature.collections.list

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionListTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewCollectionList()
        }
    }

    @Test
    fun editMode() {
        snapshot {
            PreviewCollectionListEditMode()
        }
    }
}
