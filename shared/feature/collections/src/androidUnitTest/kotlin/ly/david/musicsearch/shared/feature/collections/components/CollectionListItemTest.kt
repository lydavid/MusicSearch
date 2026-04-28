package ly.david.musicsearch.shared.feature.collections.components

import com.google.testing.junit.testparameterinjector.TestParameter
import ly.david.musicsearch.shared.domain.listitem.CollectionContainsEntities
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollectionListItemTest : ScreenshotTest() {

    @Test
    fun isRemoteVisitedCollected(
        @TestParameter isRemote: Boolean,
        @TestParameter visited: Boolean,
        @TestParameter containsEntities: CollectionContainsEntities?,
    ) {
        snapshot {
            PreviewCollectionListItem(
                isRemote = isRemote,
                visited = visited,
                containsEntities = containsEntities,
            )
        }
    }

    @Test
    fun disabled() {
        snapshot {
            PreviewCollectionListItemDisabled()
        }
    }

    @Test
    fun highlighted() {
        snapshot {
            PreviewCollectionListItemHighlighted()
        }
    }
}
