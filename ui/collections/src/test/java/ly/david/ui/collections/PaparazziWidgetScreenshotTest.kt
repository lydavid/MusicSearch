package ly.david.ui.collections

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
abstract class PaparazziWidgetScreenshotTest(
    config: DeviceConfig
) : PaparazziScreenshotTest(config) {

    @get:Rule
    override val paparazzi = Paparazzi(
        deviceConfig = config,
        theme = PAPARAZZI_THEME,
        renderingMode = RenderingMode.SHRINK,
        showSystemUi = false
    )
}
