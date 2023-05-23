package ly.david.ui.common.dialog

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.ui.common.PaparazziScreenshotTest
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SimpleAlertDialogTest(
    config: DeviceConfig
) : PaparazziScreenshotTest(config) {

    @Test
    fun long() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    SimpleAlertDialog(
                        title = "Title text that is long enough to wrap",
                        confirmText = "OK"
                    ) {}
                }
            }
        }
    }
}
