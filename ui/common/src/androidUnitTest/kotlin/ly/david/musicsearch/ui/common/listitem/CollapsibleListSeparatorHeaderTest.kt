package ly.david.musicsearch.ui.common.listitem

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class CollapsibleListSeparatorHeaderTest : ScreenshotTest() {

    @Test
    fun expanded() {
        snapshot {
            PreviewCollapsibleListSeparatorHeaderExpanded()
        }
    }

    @Test
    fun collapsed() {
        snapshot {
            PreviewCollapsibleListSeparatorHeaderCollapsed()
        }
    }
}
