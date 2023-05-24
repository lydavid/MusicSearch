package ly.david.ui.common.listitem

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import ly.david.ui.common.PaparazziWidgetScreenshotTest
import ly.david.ui.common.R
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ListSeparatorHeaderTest(
    config: DeviceConfig
) : PaparazziWidgetScreenshotTest(config) {

    @Test
    fun listSeparatorHeader() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    ListSeparatorHeader("Album + Compilation")
                }
            }
        }
    }

    @Test
    fun attributesListSeparatorHeader() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    AttributesListSeparatorHeader(R.string.work)

                }
            }
        }
    }

    @Test
    fun informationListSeparatorHeader() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    InformationListSeparatorHeader(R.string.area)
                }
            }
        }
    }
}
