package ly.david.ui.collections

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CreateCollectionDialogTest(
    config: DeviceConfig
) : PaparazziScreenshotTest(config) {

    @Test
    fun default() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    CreateCollectionDialog()
                }
            }
        }
    }
}
