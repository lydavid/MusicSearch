package ly.david.musicsearch.ui.common.releasegroup

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ReleaseGroupListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewReleaseGroupListItem()
        }
    }

    @Test
    fun withType() {
        snapshot {
            PreviewReleaseGroupListItemWithType()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewReleaseGroupListItemVisited()
        }
    }
}
