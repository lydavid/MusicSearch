package ly.david.ui.common.dialog

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Rule
import org.junit.Test

class SimpleAlertDialogTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5.copy(nightMode = NightMode.NIGHT),
        theme = "android:Theme.Material.Light.NoActionBar",
    )

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
