package ly.david.musicsearch.shared.feature.collections.components

import com.google.testing.junit.testparameterinjector.TestParameter
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionListItemTest : ScreenshotTest() {

    @Test
    fun isRemoteVisited(
        @TestParameter isRemote: Boolean,
        @TestParameter visited: Boolean,
    ) {
        snapshot {
            PreviewCollectionListItem(isRemote, visited)
        }
    }
}
