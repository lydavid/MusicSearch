package ly.david.musicsearch.shared.feature.collections.list

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionListUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewCollectionListUi()
        }
    }

    @Test
    fun editMode() {
        snapshot {
            PreviewCollectionListUiSelection()
        }
    }
}
