package ly.david.ui.common.text

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.ui.common.PaparazziWidgetScreenshotTest
import ly.david.ui.common.R
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TextWithHeadingTest(
    config: DeviceConfig
) : PaparazziWidgetScreenshotTest(config) {

    @Test
    fun default() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    TextWithHeadingRes(headingRes = R.string.format, text = "Digital Media")
                }
            }
        }
    }
}
