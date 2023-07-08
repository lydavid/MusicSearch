package ly.david.ui.common.dialog

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class MultipleChoiceDialogTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            MultipleChoiceDialog(
                title = "Theme",
                choices = listOf(
                    "Light",
                    "Dark",
                    "System"
                ),
                selectedChoiceIndex = 0,
                onSelectChoiceIndex = {}
            )
        }
    }
}
