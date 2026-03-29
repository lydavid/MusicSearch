package ly.david.musicsearch.shared.feature.collections.add

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewCollectionBottomSheetContent()
        }
    }

    @Test
    fun syncing() {
        snapshot {
            PreviewCollectionBottomSheetContentSyncing()
        }
    }
}
