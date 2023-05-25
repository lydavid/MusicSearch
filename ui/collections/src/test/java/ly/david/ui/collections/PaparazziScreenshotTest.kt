package ly.david.ui.collections

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

const val PAPARAZZI_THEME = "android:Theme.Material.Light.NoActionBar"

// TODO: any way to not have to duplicate this across modules?
//  Cannot use a test module like test-data because Paparazzi isn't applied in main src set
/**
 * Common setup for running screenshot tests on light and dark mode.
 *
 * Unfortunately, implementing class must also be annotated with:
 * ```
 * @RunWith(Parameterized::class)
 * ```
 *
 * and declare this parameter:
 * ```
 * config: DeviceConfig
 * ```
 */
@RunWith(Parameterized::class)
abstract class PaparazziScreenshotTest( // Must be abstract. Ignore Detekt.
    config: DeviceConfig
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<DeviceConfig> {
            return listOf(
                DeviceConfig.PIXEL_5,
                DeviceConfig.PIXEL_5.copy(nightMode = NightMode.NIGHT)
            )
        }
    }

    @get:Rule
    open val paparazzi = Paparazzi(
        deviceConfig = config,
        theme = PAPARAZZI_THEME
    )
}
