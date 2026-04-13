package ly.david.musicsearch.shared.feature.collections.components

import com.android.resources.NightMode
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.BaseScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CollectionListItemTest(
    @param:TestParameter override var nightMode: NightMode,
) : BaseScreenshotTest() {

    @Test
    fun isRemoteVisitedCollected(
        @TestParameter isRemote: Boolean,
        @TestParameter visited: Boolean,
        @TestParameter collected: Boolean,
    ) {
        snapshot {
            PreviewCollectionListItem(
                isRemote = isRemote,
                visited = visited,
                collected = collected,
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
