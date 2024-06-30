package ly.david.musicsearch.shared.feature.collections.components

import com.google.testing.junit.testparameterinjector.TestParameter
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionListItemTest : ScreenshotTest() {

    @Test
    fun isRemote(
        @TestParameter isRemote: Boolean,
    ) {
        snapshot {
            PreviewCollectionListItem(isRemote)
        }
    }
}
