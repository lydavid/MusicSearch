package ly.david.ui.common.component

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
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
    fun withIcon() {
        snapshot {
            PreviewClickableItemWithIcon()
        }
    }
}
