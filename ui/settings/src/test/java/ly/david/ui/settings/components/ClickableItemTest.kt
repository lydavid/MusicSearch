package ly.david.ui.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.settings.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ClickableItemTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            ClickableItem(
                text = "Click me"
            )
        }
    }
}
