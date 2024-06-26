package ly.david.musicsearch.ui.common.component

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ClickableItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewClickableItem()
        }
    }

    @Test
    fun withSubtitle() {
        snapshot {
            PreviewClickableItemWithSubtitle()
        }
    }

    @Test
    fun withStartIcon() {
        snapshot {
            PreviewClickableItemWithStartIcon()
        }
    }

    @Test
    fun withEndIcon() {
        snapshot {
            PreviewClickableItemWithEndIcon()
        }
    }

    @Test
    fun withStartEndIcon() {
        snapshot {
            PreviewClickableItemWithStartEndIcon()
        }
    }
}
