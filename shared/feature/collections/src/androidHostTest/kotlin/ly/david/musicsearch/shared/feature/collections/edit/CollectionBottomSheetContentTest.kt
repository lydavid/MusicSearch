package ly.david.musicsearch.shared.feature.collections.edit

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
    fun noConfirm() {
        snapshot {
            PreviewCollectionBottomSheetContentNoConfirm()
        }
    }

    @Test
    fun withQuery() {
        snapshot {
            PreviewCollectionBottomSheetContentWithQuery()
        }
    }

    @Test
    fun applyEnabled() {
        snapshot {
            PreviewCollectionBottomSheetContentApplyEnabled()
        }
    }

    @Test
    fun noItems() {
        snapshot {
            PreviewCollectionBottomSheetContentNoItems()
        }
    }
    // There's some timing issue
//    @Test
//    fun syncing() {
//        snapshot {
//            PreviewCollectionBottomSheetContentSyncing()
//        }
//    }
}
