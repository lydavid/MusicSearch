package ly.david.ui.common.dialog

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.ui.common.PaparazziScreenshotTest
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MultipleChoiceDialogTest(
    config: DeviceConfig
): PaparazziScreenshotTest(config) {

    @Test
    fun default() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
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
    }
}
