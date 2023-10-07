package ly.david.ui.common.listitem

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ListSeparatorHeaderTest : PaparazziScreenshotTest() {

    @Test
    fun listSeparatorHeader() {
        snapshot {
            ListSeparatorHeaderPreview()
        }
    }
}
