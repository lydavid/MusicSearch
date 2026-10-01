package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ScrollableTopAppBarTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewScrollableTopAppBar()
        }
    }

    @Test
    fun withIcon() {
        snapshot {
            PreviewScrollableTopAppBarWithIcon()
        }
    }

    @Test
    fun withTabs() {
        snapshot {
            PreviewScrollableTopAppBarWithTabs()
        }
    }
}
