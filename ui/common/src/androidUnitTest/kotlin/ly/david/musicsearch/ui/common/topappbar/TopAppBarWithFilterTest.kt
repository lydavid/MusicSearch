package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TopAppBarWithFilterTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewTopAppBarWithFilter()
        }
    }

    @Test
    fun filterMode() {
        snapshot {
            PreviewTopAppBarWithFilterFilterMode()
        }
    }

    @Test
    fun filterModeWithText() {
        snapshot {
            PreviewTopAppBarWithFilterFilterModeWithText()
        }
    }

    @Test
    fun noFilter() {
        snapshot {
            PreviewTopAppBarWithFilterNoFilter()
        }
    }

    @Test
    fun withTabs() {
        snapshot {
            PreviewTopAppBarWithFilterWithTabs()
        }
    }

    @Test
    fun withTabsFilterMode() {
        snapshot {
            PreviewTopAppBarWithFilterWithTabsFilterMode()
        }
    }

    @Test
    fun selectionDeselectAll() {
        snapshot {
            PreviewTopAppBarWithFilterWithWithSelectionDeselectAll()
        }
    }

    @Test
    fun selectionSelectAll() {
        snapshot {
            PreviewTopAppBarWithFilterWithSelectionSelectAll()
        }
    }
}
